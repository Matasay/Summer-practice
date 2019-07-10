package summerprac.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseItem {
    private String email;
    private long cash_voucher_first;
    private long cash_voucher_last;
    private short copies_count;
    private String price_copy;
}
