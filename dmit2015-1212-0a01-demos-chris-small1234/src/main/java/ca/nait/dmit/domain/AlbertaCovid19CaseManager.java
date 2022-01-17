package ca.nait.dmit.domain;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AlbertaCovid19CaseManager {

    @Getter
    private List<AlbertaCovid19CaseData> albertaCovid19CaseDataList;

    public AlbertaCovid19CaseManager() throws IOException {
        albertaCovid19CaseDataList = loadCsvData();
    }

    private List<AlbertaCovid19CaseData> loadCsvData() throws IOException {
                List<AlbertaCovid19CaseData> dataList = new ArrayList<>();
                try (var reader = new BufferedReader(new InputStreamReader(
                        getClass().getResourceAsStream("/data/covid-19-alberta-statistics-case-data.csv")))) {
                    final var delimiter = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
                    String line;
                    // Skip the first line as it contains column headings
                    reader.readLine();
                    var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    // Read one line at time from the input stream
                    while ( (line = reader.readLine()) != null) {
                        String[] values = line.split(delimiter, -1); // The -1 limit allows for any number of fields and not discard trailing empty fields
                        // Column order of fields:
                        // 0 - "ID"
                        // 1 - "Date reported"
                        // 2 - "Alberta Health Services Zone"
                        // 3 - "Gender"
                        // 4 - "Age group
                        // 5 - "Case status"
                        // 6 - "Case type"

                        // Create an object from each row in the file
                        AlbertaCovid19CaseData lineData = new AlbertaCovid19CaseData();
                        lineData.setId(Integer.parseInt(values[0].replaceAll("\"", "")));
                        lineData.setDateReported(LocalDate.parse(values[1], dateFormatter));
                        lineData.setAhsZone(values[2].replaceAll("\"",""));
                        lineData.setGender(values[3].replaceAll("\"",""));
                        lineData.setAgeGroup(values[4].replaceAll("\"",""));
                        lineData.setCaseStatus(values[5].replaceAll("\"",""));
                        lineData.setCaseType(values[6].replaceAll("\"",""));
                        // Add lineData to dataList
                        dataList.add(lineData);
                    }
                }
                return dataList;
            }
}
