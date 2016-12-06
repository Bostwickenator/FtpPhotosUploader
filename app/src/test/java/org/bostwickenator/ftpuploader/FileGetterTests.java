package org.bostwickenator.ftpuploader;


import junit.framework.Assert;

import org.junit.Test;

public class FileGetterTests {

    @Test
    public void testNameRejection(){
        Assert.assertEquals(FileGetter.checkIfValidFilename("bob.test"), false);
        Assert.assertEquals(FileGetter.checkIfValidFilename("test.bob"), true);
        Assert.assertEquals(FileGetter.checkIfValidFilename("test2.BOB"), true);
        Assert.assertEquals(FileGetter.checkIfValidFilename("test"), false);
    }
}
