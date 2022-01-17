package ca.nait.dmit.domain;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AlbertaCovid19CaseManagerTest {
    @Test
    void getAlbertaCovid19CaseList() throws IOException {
        AlbertaCovid19CaseManager caseManager = new AlbertaCovid19CaseManager();
        assertEquals(436495, caseManager.getAlbertaCovid19CaseDataList().size());
    }
}