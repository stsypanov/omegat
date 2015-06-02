package org.omegat.experemental;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by Сергей on 29.05.15.
 */
public class IoVsNioTest {

    @Test
    public void readFileWithStandardIo() throws Exception{

    }

    @Test
    public void readFileWithNio() throws Exception{
        Path path = FileSystems.getDefault().getPath("smallFile.txt");
        List<String> smallFilesLines =  Files.readAllLines(path, StandardCharsets.UTF_8);


    }

    @Test
    public void readFileWithCommonsIO() throws Exception{

    }
}
