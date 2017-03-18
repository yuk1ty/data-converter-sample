package loader;

/*
* Copyright 2017 Yuki Toyoda
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ScenarioDataLoader {

    private static final TsvParserSettings SETTINGS;

    static {
        SETTINGS = new TsvParserSettings();
        SETTINGS.getFormat().setLineSeparator("\r\n");
        SETTINGS.setHeaderExtractionEnabled(true);
    }

    private final String charsetName;

    private final String rootPath;

    public ScenarioDataLoader(String charsetName, String rootPath) {
        this.charsetName = charsetName;
        this.rootPath = rootPath;
    }

    /**
     * 対象のtsvファイルを読み込んでそのまま返します。
     * @return 読み込んだ結果
     */
    public Map<Path, List<String[]>> load() {
        TsvParser parser = new TsvParser(SETTINGS);
        List<Path> targetFileNameList = getTargetFileNames();
        Map<Path, List<String[]>> results = Maps.newHashMap();

        targetFileNameList.forEach(readRow(results, parser));

        return results;
    }

    private Consumer<Path> readRow(Map<Path, List<String[]>> results, TsvParser parser) {
        return filePath -> {
            try (BufferedReader br = Files.newBufferedReader(filePath, Charset.forName(charsetName))) {
                results.put(filePath, parser.parseAll(br));
            } catch (IOException e) {
                System.out.println("ファイルの読み込みに失敗しました。");
            }
        };
    }

    private List<Path> getTargetFileNames() {
        Path rootDirectoryPath = Paths.get(rootPath);
        List<Path> fetchedResults = Lists.newArrayList();

        try {
            Files.walkFileTree(rootDirectoryPath, new FileVisitor(fetchedResults));
        } catch (IOException e) {
            System.out.println("File Visitに失敗しました。");
        }
        return Collections.emptyList();
    }

    private static class FileVisitor extends SimpleFileVisitor<Path> {

        private final List<Path> fetchedResults;

        FileVisitor(List<Path> fetchedResults) {
            this.fetchedResults = fetchedResults;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            fetchedResults.add(file);
            return FileVisitResult.CONTINUE;
        }
    }
}
