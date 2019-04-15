package util;


import javafx.util.StringConverter;
import model.fxModel.instrumentStorehouseFxModel;
import model.fxModel.storehouseFxModel;
import model.storehouseModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Klasa zawiera 3 metody służące do konwersji.
 */
public class Converter {
    /**
     * Metoda do konwersji daty na format ISO. Wykorzystywana w kontrolerach DatePicker
     */
    public static StringConverter<LocalDate> converter;
    public static StringConverter<LocalDate> getConverter(){
        converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd");
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        return converter;
    }
    /**
     * Metoda konwertuje obiekt typu storehouseModel na obiekt typu storehouseFxModel
     * @param model
     * @return obiekt typu storehouseFxModel
     */
    public static storehouseFxModel convertToStorehouseFx(storehouseModel model){
        storehouseFxModel modelFx=new storehouseFxModel();
        modelFx.setIndexOfStorehouseModelList(0);
        modelFx.setIdInstrument(model.getIdStorehouse());
        modelFx.setInstrumentName(model.getInstrument().getInstrumentName().getInstrumentName());
        modelFx.setInstrumentType(model.getInstrument().getInstrumentType().getInstrumentType());
        modelFx.setInstrumentProducer(model.getInstrument().getInstrumentProducer().getInstrumentProducer());
        modelFx.setSerialNumber(model.getInstrument().getSerialNumber());
        modelFx.setIdentificationNumber(model.getInstrument().getIdentificationNumber());
        modelFx.setInstrumentRange(model.getInstrument().getInstrumentRange().getInstrumentRange());
        modelFx.setClient(model.getInstrument().getClient().getShortName());
        modelFx.setAddDate(model.getAddDate());
        modelFx.setCalibrationDate(model.getCalibrationDate());
        modelFx.setLeftDate(model.getLeftDate());
        return modelFx;
    }
    /**
     * Metoda konwertuje obiekt typu storehouseModel na obiekt typu instrumentStorehouseFxModel
     * @param model
     * @return obiekt typu storehouseFxModel
     */
    public static instrumentStorehouseFxModel convertToInstrumentStorehouseFx(storehouseModel model){
        instrumentStorehouseFxModel modelFx=new instrumentStorehouseFxModel();
        if(model.getUserWhoAdd()!=null) {
            modelFx.setAddPerson(model.getUserWhoAdd().getLogin());
        }else {
            modelFx.setAddPerson("");
        }
        if(model.getUserWhoCalibrate()!=null) {
            modelFx.setCalibratePerson(model.getUserWhoCalibrate().getLogin());
        }else {
            modelFx.setCalibratePerson("");
        }
        if(model.getUserWhoLeft()!=null) {
            modelFx.setLeftPerson(model.getUserWhoLeft().getLogin());
        }else {
            modelFx.setLeftPerson("");
        }
        modelFx.setAddDate(model.getAddDate());
        modelFx.setCalibrationDate(model.getCalibrationDate());
        modelFx.setLeftDate(model.getLeftDate());
        return modelFx;
    }
}