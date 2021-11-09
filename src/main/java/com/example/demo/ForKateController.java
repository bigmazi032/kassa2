package com.example.demo;

import com.example.demo.service.BuyTicketServiceImpl;
import com.example.demo.service.dto.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ForKateController {

    private BuyTicketServiceImpl buyTicketServiceImpl = new BuyTicketServiceImpl();
    private ReceiptDto receipt;

    @FXML
    private StackPane stackPane;

    @FXML
    private Pane buyTicketPn;

    @FXML
    private Pane returnTicketPn;

    @FXML
    private Pane schedulerPn;

    @FXML
    private Label freeSeatsLbl;

    @FXML
    private Label priceLbl;

    @FXML
    private Label summaryPrice;

    @FXML
    private ComboBox<TheatreDto> theatreCmb;

    @FXML
    private ComboBox<PerformanceDto> performanceCmb;

    @FXML
    private ComboBox<SchedulerDto> dateCmb;

    @FXML
    private ComboBox<SeatCategoryDto> categorySeatCmb;

    @FXML
    private DigitTextField quantitySeatsFld;
    //private TextField quantitySeatsFld;

    @FXML
    private Button buyBtn;

    @FXML
    private TableView<ReceiptItemTblView> shoppingTbl;

    @FXML
    private TableColumn<ReceiptItemTblView, String> theatreNameClmn;

    @FXML
    private TableColumn<ReceiptItemTblView, String> performanceNameClmn;

    @FXML
    private TableColumn<ReceiptItemTblView, String> dateClmn;

    @FXML
    private TableColumn<ReceiptItemTblView, String> seatCategoryNameClmn;

    @FXML
    private TableColumn<ReceiptItemTblView, Long> quantityClmn;
    @FXML
    private TableColumn<ReceiptItemTblView, Long> summClmn;

    ObservableList<ReceiptItemTblView> data;

    @FXML
    public void initialize() {
        System.out.println("инициализируем таблицу");
        initTable();
    }

    private void initTable() {
        theatreNameClmn.setCellValueFactory(
                new PropertyValueFactory<ReceiptItemTblView, String>("theatreName"));
        performanceNameClmn.setCellValueFactory(
                new PropertyValueFactory<ReceiptItemTblView, String>("performanceName"));
        dateClmn.setCellValueFactory(
                new PropertyValueFactory<ReceiptItemTblView, String>("dateStr"));
        seatCategoryNameClmn.setCellValueFactory(
                new PropertyValueFactory<ReceiptItemTblView, String>("seatCategoryName"));
        quantityClmn.setCellValueFactory(
                new PropertyValueFactory<ReceiptItemTblView, Long>("quantity"));
        summClmn.setCellValueFactory(
                new PropertyValueFactory<ReceiptItemTblView, Long>("summ"));

        data = FXCollections.observableArrayList();
        shoppingTbl.setItems(data);
    }


    @FXML
    protected void selectByTicketPn() {
        setAllPanesInvisible();
        buyTicketPn.setVisible(true);
        receipt = null;
        startShopping();
    }

    private void startShopping() {
        clearTheatreCmb();
        clearPerformanceCmb();
        clearDateCmb();
        clearSeatCategoryCmb();
        clearFreeSeats();
        clearPrice();
        clearQuantitySeatsFld();
        fillTheatreCmb();
    }

    private void fillTheatreCmb() {
        List<TheatreDto> theatres = buyTicketServiceImpl.getTheatreFromScheduler();
        theatreCmb.setItems(FXCollections.observableList(theatres));
        theatreCmb.setCellFactory(new Callback<ListView<TheatreDto>, ListCell<TheatreDto>>() {
            @Override
            public ListCell<TheatreDto> call(ListView<TheatreDto> theatreDtoListView) {
                return new ListCell<TheatreDto>() {
                    @Override
                    protected void updateItem(TheatreDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
        theatreCmb.setConverter(new StringConverter<TheatreDto>() {
            @Override
            public String toString(TheatreDto theatreDto) {
                if (theatreDto == null) {
                    return null;
                } else {
                    return theatreDto.getName();
                }
            }

            @Override
            public TheatreDto fromString(String s) {
                return null;
            }
        });
    }


    @FXML
    protected void selectReturnTicketPn() {
        setAllPanesInvisible();
        returnTicketPn.setVisible(true);
    }

    @FXML
    protected void selectSchedulerPn() {
        setAllPanesInvisible();
        schedulerPn.setVisible(true);
    }

    @FXML
    protected void selectTheatre(Event event) {
        clearPerformanceCmb();
        clearDateCmb();
        clearSeatCategoryCmb();
        clearFreeSeats();
        clearPrice();
        clearQuantitySeatsFld();
        if (!isEmpty(event)) {
            fillPerformanceCmb();
        }
    }

    private void fillPerformanceCmb() {
        List<PerformanceDto> performances = buyTicketServiceImpl.getPerformanceFromScheduler(theatreCmb.getSelectionModel().getSelectedItem());
        performanceCmb.setItems(FXCollections.observableList(performances));
        performanceCmb.setCellFactory(new Callback<ListView<PerformanceDto>, ListCell<PerformanceDto>>() {
            @Override
            public ListCell<PerformanceDto> call(ListView<PerformanceDto> theatreDtoListView) {
                return new ListCell<PerformanceDto>() {
                    @Override
                    protected void updateItem(PerformanceDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
        performanceCmb.setConverter(new StringConverter<PerformanceDto>() {
            @Override
            public String toString(PerformanceDto performanceDto) {
                if (performanceDto == null) {
                    return null;
                } else {
                    return performanceDto.getName();
                }
            }

            @Override
            public PerformanceDto fromString(String s) {
                return null;
            }
        });
    }

    @FXML
    protected void selectPerformance(Event event) {
        clearDateCmb();
        clearSeatCategoryCmb();
        clearFreeSeats();
        clearPrice();
        clearQuantitySeatsFld();
        if (!isEmpty(event)) {
            fillDateCmb();
        }
    }

    private void fillDateCmb() {
        List<SchedulerDto> schedulers = buyTicketServiceImpl.getScheduler(theatreCmb.getSelectionModel().getSelectedItem(),
                performanceCmb.getSelectionModel().getSelectedItem());
        dateCmb.setItems(FXCollections.observableList(schedulers));
        dateCmb.setCellFactory(new Callback<ListView<SchedulerDto>, ListCell<SchedulerDto>>() {
            @Override
            public ListCell<SchedulerDto> call(ListView<SchedulerDto> theatreDtoListView) {
                return new ListCell<SchedulerDto>() {
                    @Override
                    protected void updateItem(SchedulerDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getDate().toString());
                        }
                    }
                };
            }
        });
        dateCmb.setConverter(new StringConverter<SchedulerDto>() {
            @Override
            public String toString(SchedulerDto schedulerDto) {
                if (schedulerDto == null) {
                    return null;
                } else {
                    return schedulerDto.getDate().toString();
                }
            }

            @Override
            public SchedulerDto fromString(String s) {
                return null;
            }
        });
    }


    @FXML
    protected void selectDate(Event event) {
        clearSeatCategoryCmb();
        clearFreeSeats();
        clearPrice();
        clearQuantitySeatsFld();
        if (!isEmpty(event)) {
            fillSeatCategoryCmb();
        }
    }

    private void fillSeatCategoryCmb() {
        List<SeatCategoryDto> seatCategories = buyTicketServiceImpl.getSeatCategories(dateCmb.getSelectionModel().getSelectedItem());
        categorySeatCmb.setItems(FXCollections.observableList(seatCategories));
        categorySeatCmb.setCellFactory(new Callback<ListView<SeatCategoryDto>, ListCell<SeatCategoryDto>>() {
            @Override
            public ListCell<SeatCategoryDto> call(ListView<SeatCategoryDto> seatCategoryDtoListView) {
                return new ListCell<SeatCategoryDto>() {
                    @Override
                    protected void updateItem(SeatCategoryDto item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
        categorySeatCmb.setConverter(new StringConverter<SeatCategoryDto>() {
            @Override
            public String toString(SeatCategoryDto seatCategoryDto) {
                if (seatCategoryDto == null) {
                    return null;
                } else {
                    return seatCategoryDto.getName();
                }
            }

            @Override
            public SeatCategoryDto fromString(String s) {
                return null;
            }
        });
    }

    @FXML
    protected void selectSeatCategory(Event event) {
        clearFreeSeats();
        clearPrice();
        clearQuantitySeatsFld();
        if (!isEmpty(event)) {
            fillPriceAndEmptySeats();
        }
    }

    private SeatStatusDto currentSeat = null;

    private void fillPriceAndEmptySeats() {
       currentSeat = buyTicketServiceImpl.getSeatStatuses(dateCmb.getSelectionModel().getSelectedItem(),
               categorySeatCmb.getSelectionModel().getSelectedItem());
        freeSeatsLbl.setText(currentSeat.getFree().toString());
        priceLbl.setText(currentSeat.getPrice().toString());
        quantitySeatsFld.setDisable(false);

    }

    @FXML
    protected void buyTicket() {
        if (Objects.isNull(receipt)) {
            receipt = new ReceiptDto();
            receipt.setDate(LocalDate.now());
            receipt.setSumma(0L);
            receipt.setSummaWithDiscount(BigDecimal.ZERO);
            receipt = buyTicketServiceImpl.createReceipt(receipt);
        }
        ReceiptItemDto receiptItem = new ReceiptItemDto();
        receiptItem.setReceiptId(receipt.getId());
        receiptItem.setSeat(currentSeat);
        receiptItem.setQuantitySeats(Long.parseLong(quantitySeatsFld.getText()));
        receiptItem = buyTicketServiceImpl.createReceiptItem(receiptItem);
        receipt.addReceiptItem(receiptItem);
        System.out.println(receipt);
        addReceiptItemInTable(receiptItem);
        startShopping();
    }

    private void addReceiptItemInTable(ReceiptItemDto receiptItem) {
        data.add(new ReceiptItemTblView(receiptItem));
    }

    @FXML
    protected void completeShopping() {
        System.out.println("Закрываем чек");
    }

    private void setAllPanesInvisible() {
        ObservableList<Node> children = stackPane.getChildren();
        children.forEach(c -> c.setVisible(false));
        int size = children.size();
        summaryPrice.setText(String.valueOf(size));
    }

    void clearTheatreCmb() {
        theatreCmb.setItems(FXCollections.emptyObservableList());
    }

    void clearPerformanceCmb() {
        performanceCmb.setItems(FXCollections.emptyObservableList());
    }
    void clearDateCmb() {
        dateCmb.setItems(FXCollections.emptyObservableList());
    }
    void clearSeatCategoryCmb() {
        categorySeatCmb.setItems(FXCollections.emptyObservableList());
    }
    void clearFreeSeats() {
        freeSeatsLbl.setText("-");
    }
    void clearPrice() {
        priceLbl.setText("-");
    }

    void clearQuantitySeatsFld() {
        quantitySeatsFld.setText("");
        quantitySeatsFld.setDisable(true);
        buyBtn.setDisable(true);
    }


    private boolean isEmpty(Event event) {
        Object selectedItem = ((ComboBox) event.getSource()).getSelectionModel().getSelectedItem();
        return Objects.isNull(selectedItem);
    }

    public void onQuantitySeatsFldFill(){
        if (Objects.isNull(quantitySeatsFld.getText()) || quantitySeatsFld.getText().isEmpty() || (priceLbl.getText().equals("-") && freeSeatsLbl.getText().equals("-"))) {
            buyBtn.setDisable(true);
            return;
        }
        long l = Long.parseLong(quantitySeatsFld.getText());
        buyBtn.setDisable(l <= 0);
    }

}
