import config.Configuration;
import exporter.DataExporter;
import loader.ScenarioDataLoader;
import parser.ScenarioDataParser;

public class ApplicationRunner {

    public static void main(String... args) {
        ApplicationRunner runner = new ApplicationRunner();
        runner.run();
    }

    private void run() {
        ScenarioDataLoader loader = new ScenarioDataLoader(Configuration.rootPath, Configuration.charsetName);
        ScenarioDataParser parser = new ScenarioDataParser();
        DataExporter exporter = new DataExporter(Configuration.exportDirectoryPath, Configuration.charsetName);
        exporter.flush(parser.parse(loader.load()));
    }
}
