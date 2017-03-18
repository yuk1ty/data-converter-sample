package exporter;

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

import com.google.common.collect.Multimap;
import com.univocity.parsers.tsv.TsvWriter;
import com.univocity.parsers.tsv.TsvWriterSettings;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.function.BiConsumer;

public class DataExporter {

    private final String exportDirectoryPath;

    private final String charsetName;

    public DataExporter(String exportDirectoryPath, String charsetName) {
        this.exportDirectoryPath = exportDirectoryPath;
        this.charsetName = charsetName;
    }

    public void flush(Multimap<Path, Long> results) {
        try (BufferedWriter bw = Files.newBufferedWriter(generateFilePathWithTimestamp(), Charset.forName(charsetName), StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            TsvWriter writer = new TsvWriter(bw, new TsvWriterSettings());

            results.asMap().forEach(flushRow(writer));

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BiConsumer<Path, Collection<Long>> flushRow(TsvWriter writer) {
        return (path, scenarioIds) -> scenarioIds.forEach(scenarioId -> writer.writeRow(path, scenarioId));
    }

    private Path generateFilePathWithTimestamp() {
        return Paths.get(exportDirectoryPath + "//result_" + System.currentTimeMillis() + ".tsv");
    }
}
