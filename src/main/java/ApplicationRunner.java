import config.Configuration;
import loader.ScenarioDataLoader;

public class ApplicationRunner {

    public static void main(String... args) {
        ApplicationRunner runner = new ApplicationRunner();
        runner.run();
    }

    private void run() {
        ScenarioDataLoader loader = new ScenarioDataLoader(Configuration.path, Configuration.charsetName);
    }
}
