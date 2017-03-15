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

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class ScenarioDataLoader {

    private static final TsvParserSettings SETTINGS;

    static {
        SETTINGS = new TsvParserSettings();
        SETTINGS.getFormat().setLineSeparator("\r\n");
        SETTINGS.setHeaderExtractionEnabled(true);
    }

    private final String charsetName;

    private final String directoryPathString;

    public ScenarioDataLoader(String charsetName, String directoryPathString) {
        this.charsetName = charsetName;
        this.directoryPathString = directoryPathString;
    }

    /**
     * 対象のtsvファイルを読み込んでそのまま返します。
     * @return 読み込んだ結果
     */
    public List<String[]> load() {
        TsvParser parser = new TsvParser(SETTINGS);

        try (BufferedReader br = Files.newBufferedReader(Paths.get(directoryPathString), Charset.forName(charsetName))) {
            return parser.parseAll(br);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }
}
