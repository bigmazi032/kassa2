package com.example.demo.service;



import com.example.demo.service.dto.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BuyTicketServiceImpl implements BuyTicketService{
    public BuyTicketServiceImpl() {
        createScheduler(15);
        createSeatStatuses();
    }

    List<TheatreDto> theatres = Arrays.asList(
            new TheatreDto(1L, "Большой театр" ),
            new TheatreDto(2L, "Малый театр" ),
            new TheatreDto(3L, "Театр на Таганке" ),
            new TheatreDto(4L, "ТЮЗ" )
    );

    List<PerformanceDto> performances = Arrays.asList(
            new PerformanceDto(5L, "Вишневый сад"),
            new PerformanceDto(6L, "Идиот"),
            new PerformanceDto(7L, "Преступление и наказание"),
            new PerformanceDto(8L, "Бесы")
    );
    List<SchedulerDto> schedulers = new ArrayList<>();

    List<SeatCategoryDto> seatCategories = Arrays.asList(
            new SeatCategoryDto(1L, "Обычная"),
            new SeatCategoryDto(2l, "VIP"));

    List<LocalDate> dates;

    List<SeatStatusDto> seatStatuses;

    @Override
    public List<TheatreDto> getTheatreFromScheduler() {
        return theatres;
    }
    @Override
    public List<PerformanceDto> getPerformanceFromScheduler(TheatreDto theatreDto) {
        return performances;
    }

    private void createDates(LocalDate dateStart, int quantity) {
        List<LocalDate> listOfDates = new ArrayList<>();
        listOfDates.add(dateStart);
        for (int i = 1; i < quantity; i++) {
            listOfDates.add(dateStart.plusDays(i));
        }
        dates = listOfDates;
    }

    private void createScheduler (int quantity) {
        List<SchedulerDto> schedulerDtoList = new ArrayList<>();
        createDates(LocalDate.now(), quantity);
        Long id = 0l;

        for (int th = 0; th < theatres.size(); th++) {
            for (int per = 0; per < performances.size(); per++) {
                for (int d = 0; d < dates.size(); d++) {
                    SchedulerDto scheduler = new SchedulerDto();
                    scheduler.setId(id++);
                    scheduler.setTheatre(theatres.get(th));
                    scheduler.setPerformance(performances.get(per));
                    scheduler.setDate(dates.get(d));
                    schedulerDtoList.add(scheduler);
                }
            }
        }
        schedulers = schedulerDtoList;
    }

    @Override
    public List<SchedulerDto> getScheduler(TheatreDto theatreDto, PerformanceDto performanceDto) {
        List<SchedulerDto> collect = schedulers.stream()
                .filter(s -> s.getTheatre().equals(theatreDto) && s.getPerformance().equals(performanceDto))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<SeatCategoryDto> getSeatCategories (SchedulerDto scheduler) {
        return seatCategories;
    }

    private void createSeatStatuses() {
        List<SeatStatusDto> seatStatusDtos = new ArrayList<>();
        Long id = 0l;
        for (int i = 0; i < schedulers.size(); i++) {
            for (int c = 0; c < seatCategories.size(); c++) {
                SeatStatusDto seatStatus = new SeatStatusDto();
                seatStatus.setId(id++);
                seatStatus.setScheduler(schedulers.get(i));
                seatStatus.setSeatCategory(seatCategories.get(c));
                Long quantitySeats = 100L + Math.round(Math.random() * 50);
                seatStatus.setTotal(quantitySeats);
                seatStatus.setFree(quantitySeats);
                Long price = 100L * (c + 1) + Math.round(Math.random() * 100);
                seatStatus.setPrice(price);
                seatStatusDtos.add(seatStatus);
            }
        }
        seatStatuses = seatStatusDtos;
    }

    @Override
    public SeatStatusDto getSeatStatuses (SchedulerDto scheduler, SeatCategoryDto seatCategory) {
        SeatStatusDto result = seatStatuses.stream()
                .filter(s -> s.getScheduler().equals(scheduler) && s.getSeatCategory().equals(seatCategory))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Отсутсвуют места в данной категории на данное представление"));

        return result;
    }

//    public ReceiptItemDto addItem(ReceiptItemDto receiptItem) {
//        // получить сит-статус
//        // проверить что мест досстаточно
//        // уменьшить количество мест
//        // создаем чек если первая заявка
//        Long receiptID = receiptItem.getReceiptId();
//        if (Objects.isNull(receiptID)) {
//            ReceiptDto receipt = new ReceiptDto();
//            receipt.setDate(LocalDate.now());
//            receipt.setSumma(0L);
//            receipt.setSummaWithDiscount(BigDecimal.ZERO);
//            receipt = createReceipt(receipt);
//            receiptID = receipt.getId();
//            receiptItem.setReceiptId(receiptID);
//        }
//        // создаем айтем чека
//        receiptItem = createReceiptItem(receiptItem);
//        // добавляем item в чек
//        return receiptItem;
//    }


    List<ReceiptDto> receiptDtos = new ArrayList<>();
    static Long receiptId = 0L;
    @Override
    public ReceiptDto createReceipt(ReceiptDto receiptDto) {
        receiptDto.setId(receiptId++);
        receiptDtos.add(receiptDto);
        return receiptDto;

        //
    }

    List<ReceiptItemDto> receiptItemDtos = new ArrayList<>();
    static Long receiptItemId = 0L;
    @Override
    public ReceiptItemDto createReceiptItem (ReceiptItemDto receiptItem) {
        receiptItem.setId(receiptItemId++);
        receiptItemDtos.add(receiptItem);
        return receiptItem;
    }
}
