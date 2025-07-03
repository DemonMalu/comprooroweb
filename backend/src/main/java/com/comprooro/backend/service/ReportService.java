package com.comprooro.backend.service;

import com.comprooro.backend.dto.*;
import com.comprooro.backend.model.*;
import com.comprooro.backend.repo.*;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
	
	private final MovimentoRepository movimentoRepo;
	private final OperazioneRepository operazioneRepo;
	private final ArticoloService articoloService;

	public ReportService(MovimentoRepository movimentoRepo, OperazioneRepository operazioneRepo, ArticoloService articoloService) {
		this.movimentoRepo = movimentoRepo;
		this.operazioneRepo = operazioneRepo;
		this.articoloService = articoloService;
	}

	public byte[] generaDichiarazioneVenditaPDF(MovimentoResponseDTO movimento, List<ArticoloResponseDTO> articoli) {
	    logger.info("Generazione del PDF per la dichiarazione di vendita - Movimento ID: {}", movimento.getIdMovimento());

	    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
	        PdfWriter writer = new PdfWriter(baos);
	        PdfDocument pdf = new PdfDocument(writer);
	        Document document = new Document(pdf);

	        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
	        document.setFont(font);

	        document.add(new Paragraph("Dichiarazione di Vendita").setBold().setFontSize(16));
	        document.add(new Paragraph("\nDettagli Movimento:"));
	        document.add(new Paragraph("ID Movimento: " + movimento.getIdMovimento()));
	        document.add(new Paragraph("Data: " + movimento.getData()));
	        document.add(new Paragraph("Modalità di Pagamento: " + movimento.getModalitaPagamento()));
	        document.add(new Paragraph("Importo: " + movimento.getImporto() + " €"));
	        document.add(new Paragraph("Cliente (Username): " + movimento.getUsername()));
	        
	        articoli = articoloService.findByMovimento(movimento.getIdMovimento());

	        document.add(new Paragraph("\nArticoli Associati:"));
	        if (articoli.isEmpty()) {
	            document.add(new Paragraph("Nessun articolo associato a questo movimento."));
	        } else {
	            Table table = new Table(new float[]{1, 3, 3, 2, 2});
	            table.addCell("ID");
	            table.addCell("Nome");
	            table.addCell("Descrizione");
	            table.addCell("Grammi");
	            table.addCell("Caratura");

	            for (ArticoloResponseDTO articolo : articoli) {
	            	table.addCell(articolo.getNome() != null ? articolo.getNome() : "N/D");
	                table.addCell(articolo.getNome());
	                table.addCell(articolo.getDescrizione() != null ? articolo.getDescrizione() : "N/D");
	                table.addCell(String.valueOf(articolo.getGrammi()));
	                table.addCell(String.valueOf(articolo.getCaratura()));
	            }
	            document.add(table);
	        }

	        document.close();
	        logger.info("PDF della dichiarazione di vendita generato con successo per il movimento ID: {}", movimento.getIdMovimento());
	        return baos.toByteArray();
	    } catch (Exception e) {
	        logger.error("Errore nella generazione del PDF della dichiarazione di vendita per il movimento ID: {}", movimento.getIdMovimento(), e);
	        throw new RuntimeException("Errore nella generazione del PDF della dichiarazione di vendita", e);
	    }
	}


    public byte[] generaCartellinoArticoloPDF(ArticoloResponseDTO savedArticolo) {
        logger.info("Generazione del PDF per il cartellino dell'articolo - Articolo ID: {}", savedArticolo.getIdArticolo());

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            document.setFont(font);

            document.add(new Paragraph("Cartellino Articolo").setBold().setFontSize(14));
            document.add(new Paragraph("ID Articolo: " + savedArticolo.getIdArticolo()));
            document.add(new Paragraph("Nome: " + savedArticolo.getNome()));
            document.add(new Paragraph("Descrizione: " + savedArticolo.getDescrizione()));
            document.add(new Paragraph("Grammi: " + savedArticolo.getGrammi()));
            document.add(new Paragraph("Caratura: " + savedArticolo.getCaratura()));

            document.close();
            logger.info("PDF del cartellino generato con successo per l'articolo ID: {}", savedArticolo.getIdArticolo());
            return baos.toByteArray();
        } catch (Exception e) {
            logger.error("Errore nella generazione del PDF del cartellino per l'articolo ID: {}", savedArticolo.getIdArticolo(), e);
            throw new RuntimeException("Errore nella generazione del PDF del cartellino articolo", e);
        }
    }
    
    public BilancioResponseDTO calcolaBilancio(LocalDate data) {
        logger.info("Calcolo del bilancio per la data: {}", data);

        BigDecimal totaleUsciteMovimenti = movimentoRepo.findByData(data)
                .stream()
                .map(Movimento::getImporto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totaleEntrateOperazioni = operazioneRepo.findByDataAndTipo(data, 1) //1 = ENTRATA
                .stream()
                .map(Operazione::getImporto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totaleUsciteOperazioni = operazioneRepo.findByDataAndTipo(data, 0) //0 = USCITA
                .stream()
                .map(Operazione::getImporto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totaleUscite = totaleUsciteMovimenti.add(totaleUsciteOperazioni);

        BigDecimal rimanenze = totaleEntrateOperazioni.subtract(totaleUscite);

        logger.info("Bilancio calcolato per {}: Entrate: {}, Uscite: {}, Rimanenze: {}",
                data, totaleEntrateOperazioni, totaleUscite, rimanenze);

        return new BilancioResponseDTO(totaleEntrateOperazioni, totaleUscite, rimanenze);
    }


}




