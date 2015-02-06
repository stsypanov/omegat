/**
 * ***********************************************************************
 * OmegaT - Computer Assisted Translation (CAT) tool
 * with fuzzy matching, translation memory, keyword search,
 * glossaries, and translation leveraging into updated projects.
 * <p/>
 * Copyright (C) 2014 Alex Buloichik
 * Home page: http://www.omegat.org/
 * Support center: http://groups.yahoo.com/group/OmegaT/
 * <p/>
 * This file is part of OmegaT.
 * <p/>
 * OmegaT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * OmegaT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * ************************************************************************
 */

package org.omegat.core.data;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.madlonkay.supertmxmerge.StmProperties;
import org.madlonkay.supertmxmerge.SuperTmxMerge;
import org.madlonkay.supertmxmerge.data.ITuv;
import org.madlonkay.supertmxmerge.data.Key;
import org.madlonkay.supertmxmerge.data.ResolutionStrategy;
import org.omegat.core.Core;
import org.omegat.core.CoreEvents;
import org.omegat.core.TestCoreInitializer;
import org.omegat.core.data.IProject.DefaultTranslationsIterator;
import org.omegat.core.events.IProjectEventListener;
import org.omegat.core.team.IRemoteRepository;
import org.omegat.core.threads.IAutoSave;
import org.omegat.gui.editor.EditorSettings;
import org.omegat.gui.editor.IEditor;
import org.omegat.gui.editor.IEditorFilter;
import org.omegat.gui.editor.IPopupMenuConstructor;
import org.omegat.gui.editor.mark.Mark;
import org.omegat.gui.main.IMainMenu;
import org.omegat.gui.main.IMainWindow;
import org.omegat.util.FileUtil;
import org.omegat.util.Language;
import org.omegat.util.OStrings;
import org.omegat.util.Preferences;
import org.omegat.util.ProjectFileStorage;

import com.vlsolutions.swing.docking.Dockable;

/**
 * Child process for concurrent modification.
 *
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public class TestTeamIntegrationChild {
	public static final Logger logger = Logger.getLogger(TestTeamIntegrationChild.class.getName());

	static final String CONCURRENT_NAME = "concurrent";

	private static long finishTime;
	private static String source;
	private static String dir;
	private static String repo;
	private static int maxDelaySeconds;
	private static int segCount;
	private static EntryKey[] key;
	private static SourceTextEntry[] ste;
	private static EntryKey keyC;
	private static SourceTextEntry steC;
	private static long num = 0;
	private static long[] v;
	private static Map<String, Long> values = new HashMap<>();

	public static void main(String[] args) throws Exception {
		if (args.length != 6) {
			logger.log(Level.SEVERE, "Wrong arguments count");
			System.exit(1);
		}
		try {
			source = args[0];
			long time = Long.parseLong(args[1]);
			dir = args[2];
			repo = args[3];
			maxDelaySeconds = Integer.parseInt(args[4]);
			segCount = Integer.parseInt(args[5]);

			finishTime = System.currentTimeMillis() + time;

			Preferences.setPreference(Preferences.TEAM_AUTHOR, source);

			// get initial project
			FileUtil.deleteTree(new File(dir));

			IRemoteRepository repository = TestTeamIntegration.createRepo(repo, dir);
			repository.checkoutFullProject(repo);

			// load project
			Core.initializeConsole(new TreeMap<String, String>());
			TestCoreInitializer.initMainWindow(mainWindow);
			TestCoreInitializer.initAutoSave(autoSave);
			TestCoreInitializer.initEditor(editor);
			ProjectProperties projectProperties = ProjectFileStorage.loadProjectProperties(new File(dir));

			Core.getAutoSave().disable();
			RealProject p = new TestRealProject(projectProperties, repository);
			Core.setProject(p);
			p.loadProject(true);
			if (p.isProjectLoaded()) {
				Core.getAutoSave().enable();
				CoreEvents.fireProjectChange(IProjectEventListener.PROJECT_CHANGE_TYPE.LOAD);
			} else {
				throw new Exception("Project can't be loaded");
			}

			key = new EntryKey[segCount];
			ste = new SourceTextEntry[segCount];
			for (int c = 0; c < segCount; c++) {
				key[c] = new EntryKey("file", source + '/' + c, null, null, null, null);
				ste[c] = new SourceTextEntry(key[c], 0, null, null, new ArrayList<ProtectedPart>());
			}
			keyC = new EntryKey("file", CONCURRENT_NAME, null, null, null, null);
			steC = new SourceTextEntry(keyC, 0, null, null, new ArrayList<ProtectedPart>());

			Random rnd = new Random();
			v = new long[segCount];
			mc:
			while (true) {
				for (int c = 1; c < segCount; c++) {
					// change concurrent segment
					changeConcurrent();
					if (System.currentTimeMillis() >= finishTime) {
						break mc;
					}
					// change /0 segment
					Thread.sleep(rnd.nextInt(maxDelaySeconds * 1000) + 10);
					checksavecheck(0);

					// change /1..N segment
					Thread.sleep(rnd.nextInt(maxDelaySeconds * 1000) + 10);
					checksavecheck(c);
				}
			}
			Core.getProject().closeProject();

			// load again and check
			ProjectFactory.loadProject(projectProperties, repository, true);
			checkAll();

			System.exit(200);
		} catch (Throwable ex) {
			logger.log(Level.SEVERE, "Error", ex);
			System.exit(1);
		}
	}

	static void changeConcurrent() throws Exception {
		checkAll();

		PrepareTMXEntry prep = new PrepareTMXEntry();
		prep.translation = String.valueOf(System.currentTimeMillis());
		Core.getProject().setTranslation(steC, prep, true, null);
	}

	static void checksavecheck(int index) throws Exception {
		checkAll();

		v[index] = ++num;
		saveTranslation(ste[index], v[index]);

		checkAll();
	}

	/**
	 * Check in memory and in file.
	 */
	static void checkAll() throws Exception {
		ProjectTMX tmx = new ProjectTMX(new Language("en"), new Language("be"), false, new File(dir
				+ "/omegat/project_save.tmx"), TestTeamIntegration.checkOrphanedCallback);
		for (int c = 0; c < segCount; c++) {
			checkTranslation(c);
			checkTranslationFromFile(tmx, c);
		}

		Core.getProject().iterateByDefaultTranslations(new DefaultTranslationsIterator() {
			public void iterate(String source, TMXEntry trans) {
				Long prev = values.get(source);
				if (prev == null) {
					prev = 0L;
				}
				long curr = Long.parseLong(trans.translation);
				if (curr < prev) {
					throw new RuntimeException(source + ": Wrong value in " + source + ": current(" + curr + ") less than previous(" + prev + ')');
				}
			}
		});
	}

	static void checkTranslation(int index) {
		TMXEntry en = Core.getProject().getTranslationInfo(ste[index]);
		String sv = en == null || !en.isTranslated() ? "" : en.translation;
		if (v[index] == 0 && sv.isEmpty()) {
			return;
		}
		if ((String.valueOf(v[index])).equals(sv)) {
			return;
		}
		throw new RuntimeException(source + ": Wrong value in " + source + '/' + index + ": expected " + v[index] + " but contains " + en.translation);
	}

	static void checkTranslationFromFile(ProjectTMX tmx, int index) throws Exception {
		TMXEntry en = tmx.getDefaultTranslation(ste[index].getSrcText());
		String sv = en == null || !en.isTranslated() ? "" : en.translation;
		if (v[index] == 0 && sv.isEmpty()) {
			return;
		}
		if ((String.valueOf(v[index])).equals(sv)) {
			return;
		}
		throw new RuntimeException(source + ": Wrong value in TMX " + source + '/' + index + ": expected "
				+ v[index] + " but contains " + sv);
	}

	/**
	 * Save new translation.
	 */
	static void saveTranslation(SourceTextEntry ste, long value) {
		PrepareTMXEntry prep = new PrepareTMXEntry();
		prep.translation = String.valueOf(value);
		Core.getProject().setTranslation(ste, prep, true, null);
		Core.getProject().saveProject();
	}

	static IAutoSave autoSave = new IAutoSave() {
		public void enable() {
		}

		public void disable() {
		}
	};

	static IEditor editor = new IEditor() {

		public void windowDeactivated() {
		}

		public void waitForCommit(int timeoutSeconds) {
		}

		public void undo() {
		}

		public void setFilter(IEditorFilter filter) {
		}

		public void setAlternateTranslationForCurrentEntry(boolean alternate) {
		}

		public void requestFocus() {
		}

		public void replaceEditTextAndMark(String text) {
		}

		public void replaceEditText(String text) {
		}

		public void removeFilter() {
		}

		public void remarkOneMarker(String markerClassName) {
		}

		public void registerUntranslated() {
		}

		public void registerPopupMenuConstructors(int priority, IPopupMenuConstructor constructor) {
		}

		public void registerIdenticalTranslation() {
		}

		public void registerEmptyTranslation() {
		}

		public void refreshViewAfterFix(List<Integer> fixedEntries) {
		}

		public void refreshView(boolean doCommit) {
		}

		public void redo() {
		}

		public void prevEntryWithNote() {
		}

		public void prevEntry() {
		}

		public void nextUntranslatedEntry() {
		}

		public void nextUniqueEntry() {
		}

		public void nextTranslatedEntry() {
		}

		public void nextEntryWithNote() {
		}

		public void nextEntry() {
		}

		public void markActiveEntrySource(SourceTextEntry requiredActiveEntry, List<Mark> marks, String markerClassName) {
		}

		public void insertText(String text) {
		}

		public void gotoHistoryForward() {
		}

		public void gotoHistoryBack() {
		}

		public void gotoFile(int fileIndex) {
		}

		public void gotoEntryAfterFix(int fixedEntry, String fixedSource) {
		}

		public void gotoEntry(String srcString, EntryKey key) {
		}

		public void gotoEntry(int entryNum) {
		}

		public EditorSettings getSettings() {
			return null;
		}

		public String getSelectedText() {
			return null;
		}

		public IEditorFilter getFilter() {
			return null;
		}

		public String getCurrentTranslation() {
			return null;
		}

		public String getCurrentFile() {
			return null;
		}

		public int getCurrentEntryNumber() {
			return 0;
		}

		public SourceTextEntry getCurrentEntry() {
			return null;
		}

		public void commitAndLeave() {
		}

		public void commitAndDeactivate() {
		}

		public void changeCase(CHANGE_CASE_TO newCase) {
		}

		public void activateEntry() {
		}
	};

	static IMainWindow mainWindow = new IMainWindow() {
		public void unlockUI() {
		}

		public void showStatusMessageRB(String messageKey, Object... params) {
		}

		public void showProgressMessage(String messageText) {
		}

		public void showMessageDialog(String message) {
		}

		public void showLengthMessage(String messageText) {
		}

		public void showErrorDialogRB(String message, Object[] args, String title) {
			logger.log(Level.SEVERE, message);
		}

		public int showConfirmDialog(Object message, String title, int optionType, int messageType)
				throws HeadlessException {
			return 0;
		}

		public void setCursor(Cursor cursor) {
		}

		public void lockUI() {
		}

		IMainMenu menu = new IMainMenu() {

			public JMenu getToolsMenu() {
				return null;
			}

			public JMenuItem getProjectRecentMenuItem() {
				return null;
			}

			public JMenu getProjectMenu() {
				return new JMenu();
			}

			public JMenu getOptionsMenu() {
				return null;
			}

			public JMenu getMachineTranslationMenu() {
				return null;
			}

			public JMenu getGlossaryMenu() {
				return null;
			}
		};

		public IMainMenu getMainMenu() {
			return menu;
		}

		public Cursor getCursor() {
			return null;
		}

		public JFrame getApplicationFrame() {
			return null;
		}

		public Font getApplicationFont() {
			return null;
		}

		public void displayWarningRB(String warningKey, String supercedesKey, Object... params) {
			logger.log(Level.SEVERE, warningKey);
		}

		public void displayWarningRB(String warningKey, Object... params) {
			logger.log(Level.SEVERE, warningKey);
		}

		public void displayErrorRB(Throwable ex, String errorKey, Object... params) {
			logger.log(Level.SEVERE, errorKey);
		}

		public void addDockable(Dockable pane) {
		}
	};

	/**
	 * Override RealProject for own merge.
	 */
	static class TestRealProject extends RealProject {
		public TestRealProject(final ProjectProperties props, IRemoteRepository repository) {
			super(props, repository);
		}

		ProjectTMX mergedTMX;
		ProjectTMX baseTMX;
		ProjectTMX headTMX;

		@Override
		protected void mergeTMX(ProjectTMX baseTMX, ProjectTMX headTMX, StringBuilder commitDetails) {
			StmProperties props = new StmProperties().setBaseTmxName(OStrings.getString("TMX_MERGE_BASE"))
					.setTmx1Name(OStrings.getString("TMX_MERGE_MINE"))
					.setTmx2Name(OStrings.getString("TMX_MERGE_THEIRS"))
					.setLanguageResource(OStrings.getResourceBundle())
					.setResolutionStrategy(new ResolutionStrategy() {
						@Override
						public ITuv resolveConflict(Key key, ITuv baseTuv, ITuv projectTuv, ITuv headTuv) {
							TMXEntry enBase = baseTuv != null ? (TMXEntry) baseTuv.getUnderlyingRepresentation() : null;
							TMXEntry enProject = projectTuv != null ? (TMXEntry) projectTuv.getUnderlyingRepresentation() : null;
							TMXEntry enHead = headTuv != null ? (TMXEntry) headTuv.getUnderlyingRepresentation() : null;
							String s = "Rebase " + src(enProject) + " base=" + tr(enBase) + " head=" + tr(enHead) + " project=" + tr(enProject);
							if (CONCURRENT_NAME.equals(enProject.source)) {
								if (v(enHead) < v(enBase)) {
									throw new RuntimeException("Rebase HEAD: wrong concurrent: " + s);
								}
								if (v(enProject) < v(enBase)) {
									throw new RuntimeException("Rebase project: wrong concurrent: " + s);
								}
								if (v(enHead) > v(enProject)) {
									logger.log(Level.SEVERE, s + ": result=head");
									return headTuv;
								} else {
									logger.log(Level.SEVERE, s + ": result=project");
									return projectTuv;
								}
							} else {
								throw new RuntimeException("Rebase error: non-concurrent entry: " + s);
							}
						}
					});
			synchronized (projectTMX) {
				ProjectTMX mergedTMX = SuperTmxMerge
						.merge(baseTMX, projectTMX, headTMX, m_config.getSourceLanguage().getLanguage(),
								m_config.getTargetLanguage().getLanguage(), props);
				projectTMX.replaceContent(mergedTMX);
			}
			commitDetails.append('\n');
			commitDetails.append(props.getReport().toString());
		}

		protected void mergeTMXOld(ProjectTMX baseTMX, ProjectTMX headTMX) {
			mergedTMX = new ProjectTMX();
			this.baseTMX = baseTMX;
			this.headTMX = headTMX;
			String s = "info";
			for (TMXEntry e : baseTMX.getDefaults()) {
				use(e);
			}
			for (TMXEntry e : headTMX.getDefaults()) {
				TMXEntry eb = baseTMX.getDefaultTranslation(e.source);
				if (CONCURRENT_NAME.equals(e.source)) { // concurrent
					if (v(eb) > v(e)) {
						throw new RuntimeException("Rebase HEAD: wrong concurrent" + s);
					}
					use(e);
				} else if (e.source.startsWith(source + '/')) { // my segments
					if (v(eb) != v(e)) {
						throw new RuntimeException("Rebase HEAD: not equals for current project" + s);
					}
				} else { // other segments
					if (v(eb) > v(e)) {
						throw new RuntimeException("Rebase HEAD: less value" + s);
					}
					use(e);
				}
			}
			for (TMXEntry e : projectTMX.getDefaults()) {
				TMXEntry em = mergedTMX.getDefaultTranslation(e.source);
				if (CONCURRENT_NAME.equals(e.source)) { // concurrent
					if (v(e) > v(em)) {
						use(e);
					}
				} else if (e.source.startsWith(source + '/')) { // my segments
					if (v(e) < v(em)) {
						throw new RuntimeException("Rebase me: less value" + s);
					}
					use(e);
				} else { // other segments
					use(em);
				}
			}

			projectTMX.replaceContent(mergedTMX);
		}

		void use(TMXEntry en) {
			EntryKey k = new EntryKey("file", en.source, null, null, null, null);
			SourceTextEntry ste = new SourceTextEntry(k, 0, null, en.source, Collections.<ProtectedPart>emptyList());
			mergedTMX.setTranslation(ste, en, true);
		}

		static long v(TMXEntry e) {
			if (e == null) {
				return 0;
			} else {
				return Long.parseLong(e.translation);
			}
		}

		static String src(TMXEntry e) {
			if (e == null) {
				return "null";
			} else {
				return e.source;
			}
		}

		static String tr(TMXEntry e) {
			if (e == null) {
				return "null";
			} else {
				return e.translation;
			}
		}

		static String trans(ProjectTMX tmx, TMXEntry e) {
			TMXEntry en = tmx.getDefaultTranslation(e.source);
			if (en == null) {
				return "null";
			} else {
				return en.translation;
			}
		}
	}
}
