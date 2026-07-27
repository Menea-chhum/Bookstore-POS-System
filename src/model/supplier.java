package model;

public class supplier {
    private int supplierId;
    private String supplierName;
    private String contactNumber;
    private String email;
    private String address;

    public supplier(int supplierId,
                    String supplierName,
                    String contactNumber,
                    String email, String address) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    public void setSupplierId(int supplierId){
        this.supplierId = supplierId;
    }
    public void setSupplierName(String supplierName){
        this.supplierName = supplierName;
    }
    public void setContactNumber(String contactNumber){
        this.contactNumber = contactNumber;
    }
    public void setEmail (String email){
        this.email = email;
    }
    public void setAddress(String address){
        this.address = address;
    }


    public int getSupplierId()
    {
        return supplierId;
    }
    public String getSupplierName()
    {
        return supplierName;
    }
    public String getContactNumber()
    {
        return contactNumber;
    }
    public String getEmail()
    {
        return email;
    }
    public String getAddress()
    {
        return address;
    }

    @Override
    public String toString() {
        return supplierName;
    }
}