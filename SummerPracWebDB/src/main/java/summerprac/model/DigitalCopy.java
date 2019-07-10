package summerprac.model;

import lombok.Data;

@Data
public class DigitalCopy {
    private String title;
    private String email;
    private long cash_voucher_first;
    private long cash_voucher_last;
    private String product_key;
}
