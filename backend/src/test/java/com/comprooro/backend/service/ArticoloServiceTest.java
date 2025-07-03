package com.comprooro.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.comprooro.backend.dto.ArticoloRequestDTO;
import com.comprooro.backend.dto.ArticoloResponseDTO;
import com.comprooro.backend.model.Articolo;
import com.comprooro.backend.model.Movimento;
import com.comprooro.backend.repo.ArticoloRepository;
import com.comprooro.backend.repo.MovimentoRepository;

@ExtendWith(MockitoExtension.class)
class ArticoloServiceTest {

    @Mock
    private ArticoloRepository articoloRepo;

    @Mock
    private MovimentoRepository movimentoRepo;

    @Mock
    private ReportService reportService;

    @Mock
    private MultipartFile fotoArticolo;

    @Mock
    private MultipartFile fotoCartellino;

    @InjectMocks
    private ArticoloServiceImpl articoloService;

    private Articolo articolo;
    private ArticoloRequestDTO articoloRequestDTO;
    private Movimento movimento;

    @BeforeEach
    void setUp() throws Exception {
        movimento = new Movimento();
        movimento.setIdMovimento(1L);

        articolo = new Articolo();
        articolo.setIdArticolo(1L);
        articolo.setNome("Bracciale");
        articolo.setDescrizione("Bracciale d'oro 18k");
        articolo.setGrammi(BigDecimal.valueOf(10.5));
        articolo.setCaratura("18");
        articolo.setMovimento(movimento);
        articolo.setFoto1("test".getBytes());
        articolo.setFoto2("test2".getBytes());
        articolo.setContentType("image/jpeg");
        articolo.setContentType("image/png");

        articoloRequestDTO = new ArticoloRequestDTO();
        articoloRequestDTO.setNome("Bracciale");
        articoloRequestDTO.setDescrizione("Bracciale d'oro 18k");
        articoloRequestDTO.setGrammi(BigDecimal.valueOf(10.5));
        articoloRequestDTO.setCaratura("18");
        articoloRequestDTO.setIdMovimento(1L);

        // Simulazione del comportamento dei file
        when(fotoArticolo.getBytes()).thenReturn("test".getBytes());
        when(fotoArticolo.getContentType()).thenReturn("image/jpeg");
        when(fotoCartellino.getBytes()).thenReturn("test2".getBytes());
        when(fotoCartellino.getContentType()).thenReturn("image/png");
    }

    @Test
    void testRegister_Success() throws Exception {
        when(movimentoRepo.findById(1L)).thenReturn(Optional.of(movimento));
        when(articoloRepo.save(any(Articolo.class))).thenReturn(articolo);

        ArticoloResponseDTO result = articoloService.register(articoloRequestDTO, fotoArticolo, fotoCartellino);

        assertNotNull(result);
        assertEquals(1L, result.getIdArticolo());
        assertEquals("Bracciale", result.getNome());
        assertEquals("18", result.getCaratura());
        assertEquals(BigDecimal.valueOf(10.5), result.getGrammi());

        verify(movimentoRepo).findById(1L);
        verify(articoloRepo).save(any(Articolo.class));
        verify(reportService).generaCartellinoArticoloPDF(any(ArticoloResponseDTO.class));
        verify(fotoArticolo).getBytes();
        verify(fotoArticolo).getContentType();
        verify(fotoCartellino).getBytes();
        verify(fotoCartellino).getContentType();
    }

    @Test
    void testRegister_MovimentoNonTrovato() {
        when(movimentoRepo.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                articoloService.register(articoloRequestDTO, fotoArticolo, fotoCartellino)
        );

        assertEquals("Movimento non trovato", ex.getMessage());

        verify(movimentoRepo).findById(1L);
        verify(articoloRepo, never()).save(any());
        verify(reportService, never()).generaCartellinoArticoloPDF(any());
    }

    @Test
    void testFindAll() {
        when(articoloRepo.findAll()).thenReturn(List.of(articolo));

        List<ArticoloResponseDTO> result = articoloService.findAll();

        assertEquals(1, result.size());
        assertEquals("Bracciale", result.get(0).getNome());

        verify(articoloRepo).findAll();
    }

    @Test
    void testFindById_Success() {
        when(articoloRepo.findById(1L)).thenReturn(Optional.of(articolo));

        ArticoloResponseDTO result = articoloService.findById(1L);

        assertNotNull(result);
        assertEquals("Bracciale", result.getNome());

        verify(articoloRepo).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(articoloRepo.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                articoloService.findById(1L)
        );

        assertEquals("Articolo non trovato", ex.getMessage());

        verify(articoloRepo).findById(1L);
    }

    @Test
    void testFindByMovimento_Success() {
        when(movimentoRepo.findById(1L)).thenReturn(Optional.of(movimento));
        when(articoloRepo.findByMovimento(movimento)).thenReturn(List.of(articolo));

        List<ArticoloResponseDTO> result = articoloService.findByMovimento(1L);

        assertEquals(1, result.size());
        assertEquals("Bracciale", result.get(0).getNome());

        verify(movimentoRepo).findById(1L);
        verify(articoloRepo).findByMovimento(movimento);
    }

    @Test
    void testFindByMovimento_NotFound() {
        when(movimentoRepo.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                articoloService.findByMovimento(1L)
        );

        assertEquals("Movimento non trovato", ex.getMessage());

        verify(movimentoRepo).findById(1L);
        verify(articoloRepo, never()).findByMovimento(any());
    }
}
