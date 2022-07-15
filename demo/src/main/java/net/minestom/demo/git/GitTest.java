package net.minestom.demo.git;

import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author LBuke (Teddeh)
 */
public final class GitTest {

    public GitTest() {
        File file = new File("./.git");
        System.out.println(file.getAbsolutePath());

        try (Repository repository = new FileRepository(file)) {
            Ref master = repository.findRef("master");
            System.out.println(master.getName());
            System.out.println(master.getObjectId().getName());

            repository.writeMergeCommitMsg("testing");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
// Get a reference
Ref master = repo.getRef("master");

// Get the object the reference points to
ObjectId masterTip = master.getObjectId();

// Rev-parse
ObjectId obj = repo.resolve("HEAD^{tree}");

// Load raw object contents
ObjectLoader loader = repo.open(masterTip);
loader.copyTo(System.out);

// Create a branch
RefUpdate createBranch1 = repo.updateRef("refs/heads/branch1");
createBranch1.setNewObjectId(masterTip);
createBranch1.update();

// Delete a branch
RefUpdate deleteBranch1 = repo.updateRef("refs/heads/branch1");
deleteBranch1.setForceUpdate(true);
deleteBranch1.delete();

// Config
Config cfg = repo.getConfig();
String name = cfg.getString("user", null, "name");
     */
}
