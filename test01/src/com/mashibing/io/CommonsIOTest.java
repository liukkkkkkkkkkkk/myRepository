package com.mashibing.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.EmptyFileFilter;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

public class CommonsIOTest {
    @Test
    public void test1(){
        System.out.println(FileUtils.sizeOf(new File("src\\com\\mashibing\\io\\aa.txt")));
        Collection<File> files = FileUtils.listFiles(new File("c:"), EmptyFileFilter.NOT_EMPTY, null);
        for (File fIle:files){
            System.out.println(fIle.getAbsolutePath());
        }

    }
}
