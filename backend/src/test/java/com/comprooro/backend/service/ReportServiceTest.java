package com.comprooro.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.comprooro.backend.dto.BilancioResponseDTO;
import com.comprooro.backend.dto.ArticoloResponseDTO;
import com.comprooro.backend.dto.MovimentoResponseDTO;
import com.comprooro.backend.model.Operazione;
import com.comprooro.backend.model.Utente;
import com.comprooro.backend.repo.MovimentoRepository;
import com.comprooro.backend.repo.OperazioneRepository;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private MovimentoRepository movimentoRepo;

    @Mock
    private OperazioneRepository operazioneRepo;

    @Mock
    private ArticoloService articoloService;

    @InjectMocks
    private ReportService reportService;

    private Operazione operazioneEntrata;
    private Operazione operazioneUscita;
    private Utente testUser;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @BeforeEach
    void setUp() {
        testUser = new Utente();
        testUser.setUsername("testuser");

        operazioneEntrata = new Operazione();
        operazioneEntrata.setIdOperazione(1L);
        operazioneEntrata.setDescrizione("Vendita oro");
        operazioneEntrata.setTipo(1);
        operazioneEntrata.setImporto(BigDecimal.valueOf(1500));
        operazioneEntrata.setData(LocalDate.parse("20-02-2024", formatter));
        operazioneEntrata.setUtente(testUser);

        operazioneUscita = new Operazione();
        operazioneUscita.setIdOperazione(2L);
        operazioneUscita.setDescrizione("Acquisto oro");
        operazioneUscita.setTipo(0);
        operazioneUscita.setImporto(BigDecimal.valueOf(800));
        operazioneUscita.setData(LocalDate.parse("20-02-2024", formatter));
        operazioneUscita.setUtente(testUser);
    }

    @Test
    void testCalcolaBilancio() {
        LocalDate data = LocalDate.of(2025, 2, 28);

        when(operazioneRepo.findByDataAndTipo(data, 1)).thenReturn(List.of(operazioneEntrata));
        when(operazioneRepo.findByDataAndTipo(data, 0)).thenReturn(List.of(operazioneUscita));
        when(movimentoRepo.findByData(data)).thenReturn(List.of());

        BilancioResponseDTO bilancio = reportService.calcolaBilancio(data);

        assertEquals(BigDecimal.valueOf(1500), bilancio.getTotaleEntrate());
        assertEquals(BigDecimal.valueOf(800), bilancio.getTotaleUscite());
        assertEquals(BigDecimal.valueOf(700), bilancio.getTotaleRimanenze());
    }

    @Test
    void testGeneraDichiarazioneVenditaPDF() {
        MovimentoResponseDTO movimento = new MovimentoResponseDTO();
        movimento.setIdMovimento(1L);
        movimento.setData(LocalDate.parse("20-02-2024", formatter));
        movimento.setModalitaPagamento("Contante");
        movimento.setImporto(BigDecimal.valueOf(1500));
        movimento.setUsername("testuser");

        ArticoloResponseDTO articolo1 = new ArticoloResponseDTO();
        articolo1.setIdArticolo(1L);
        articolo1.setNome("Anello d'oro");
        articolo1.setDescrizione("Anello con diamante");
        articolo1.setGrammi(new BigDecimal(10));
        articolo1.setCaratura("18");

        when(articoloService.findByMovimento(movimento.getIdMovimento())).thenReturn(List.of(articolo1));

        byte[] pdfContent = reportService.generaDichiarazioneVenditaPDF(movimento, List.of(articolo1));

        assertNotNull(pdfContent);
        assertTrue(pdfContent.length > 0);  // Verifica che il PDF non sia vuoto
    }

    @Test
    void testGeneraCartellinoArticoloPDF() {
        ArticoloResponseDTO articolo = new ArticoloResponseDTO();
        articolo.setIdArticolo(1L);
        articolo.setNome("Anello d'oro");
        articolo.setDescrizione("Anello con diamante");
        articolo.setGrammi(new BigDecimal(10));
        articolo.setCaratura("18");

        byte[] pdfContent = reportService.generaCartellinoArticoloPDF(articolo);

        assertNotNull(pdfContent);
        assertTrue(pdfContent.length > 0);  // Verifica che il PDF non sia vuoto
    }
}
