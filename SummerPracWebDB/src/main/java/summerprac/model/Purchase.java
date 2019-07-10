package summerprac.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Purchase {
    private long cash_voucher_first;
    private long cash_voucher_last;
    private String email;
    private String payment_datetime;
    private String payment_amount;
}
