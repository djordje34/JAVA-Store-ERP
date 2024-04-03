package com.project.store.sales.service;

import com.project.store.sales.entity.Invoice;
import com.project.store.sales.entity.Order;
import com.project.store.sales.repository.InvoiceRepository;
import com.project.store.sales.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Autowired
    public InvoiceService(InvoiceRepository invoiceRepository){
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Optional<Invoice> getInvoiceById(Long id) {
        return invoiceRepository.findById(id);
    }

    public Invoice saveInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public void deleteInvoice(Long id){
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        invoice.ifPresent(invoiceRepository::delete);
    }
}
