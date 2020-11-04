package assets.asset;

import lombok.Data;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data // for Lombok autogenerated methods
@Entity // JPA-based data storing
public class Asset implements Comparable<Asset> {

    public Asset(){
    }

    private @Id @GeneratedValue Long id;

    private LocalDateTime creationDate;
    public LocalDateTime lastModified;
    private String name;
    private Double amount;
    private BigDecimal price;
    public BigDecimal percentage;
    private String ticker;
    private String typeOfAsset;
    public static BigDecimal totalValue = BigDecimal.valueOf(0.00);

    public Asset(String name, String ticker, Double amount, String typeOfAsset) throws IOException {
        this.creationDate = LocalDateTime.now();
        this.lastModified = LocalDateTime.now();
        this.name = name;
        this.ticker = ticker;
        this.amount = amount;
        this.typeOfAsset = typeOfAsset;
        this.price = setPrice(ticker);
        this.setTotalValue();
    }

    public void setTotalValue() {
        BigDecimal amount1 = BigDecimal.valueOf(this.amount);
        Asset.totalValue = totalValue.add(this.getPrice().multiply(amount1));
    }

    public BigDecimal setPrice(String ticker) throws IOException {
        Stock stock = YahooFinance.get(this.getTicker());
        return stock.getQuote().getPrice();
    }

    public BigDecimal setPercentage() {
        BigDecimal amount1 = BigDecimal.valueOf(this.amount);
        return ((this.getPrice().multiply(amount1)).divide(Asset.totalValue, 2, RoundingMode.HALF_UP)).scaleByPowerOfTen(2);
    }

    // CompareTo - When using sorted method it will use this class Comparable interface to compare between objects by the parameter stated
    // in the method compareTo(), I decided this parameter will be the percentage from the portfolio.

    @Override
    public int compareTo(Asset o) {
        return this.getPercentage().compareTo(o.getPercentage());
    }


}