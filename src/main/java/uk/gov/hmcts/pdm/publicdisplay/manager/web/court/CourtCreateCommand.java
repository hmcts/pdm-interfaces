package uk.gov.hmcts.pdm.publicdisplay.manager.web.court;

public class CourtCreateCommand extends AbstractCourtCommand {

    private int courtId;
    
    private int addressId;
    
    
    public int getCourtId() {
        return courtId;
    }
    
    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }
    
    public int getAddressId() {
        return addressId;
    }
    
    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
