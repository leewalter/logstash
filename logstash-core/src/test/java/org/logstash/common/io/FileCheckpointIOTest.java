package org.logstash.common.io;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.logstash.ackedqueue.Checkpoint;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class FileCheckpointIOTest {
    private String checkpointFolder;
    private CheckpointIO io;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        checkpointFolder = temporaryFolder
                .newFolder("checkpoints")
                .getPath();
        io = new FileCheckpointIO(checkpointFolder);
        Checkpoint checkpoint = new Checkpoint(0, 0, 1L, 8L, 10);
        io.writeHead(checkpoint);
    }

    @Test
    public void read() throws Exception {
        io = new FileCheckpointIO(checkpointFolder);
        Checkpoint chk = io.readHead();
        assertThat(chk.getMinSeqNum(), is(8L));
    }

    @Test
    public void write() throws Exception {
        String checkpointFileName = "checkpoint.6";
        io.writeTail(6, 2, 10L, 8L, 200);
        Path fullFileName = Paths.get(checkpointFolder, checkpointFileName);
        byte[] contents = Files.readAllBytes(fullFileName);
        Path path = Paths.get(checkpointFolder, checkpointFileName);
        byte[] compare = Files.readAllBytes(path);
        assertThat(contents, is(equalTo(compare)));
    }
}