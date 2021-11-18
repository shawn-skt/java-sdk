package org.fisco.bcos.sdk.codec.datatypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import org.fisco.bcos.sdk.codec.Utils;


/** Common numeric type. */
public abstract class NumericType implements Type<BigInteger> {

    private String type;
    // common integer part
    BigInteger value;
    BigInteger nValue;
    // decimal part
    private BigDecimal dValue;
    private BigDecimal decimalValue;
    private int bitSize;
    private int nbitSize;

    public NumericType(String type, BigInteger value, int bitSize) {
        this.type = type;
        this.value = value;
        this.bitSize = bitSize;
    }

    public NumericType(String type, BigDecimal value, int mbitSize, int nbitSize) {
        this.type = type;
        this.decimalValue = BigDecimal.valueOf(value.doubleValue());
        if(Utils.divideFixed(value).getKey().signum()<0&&Utils.divideFixed(value).getValue().signum()!=0){
            this.value = Utils.divideFixed(value).getKey().subtract(new BigInteger("1"));
            
        }else {
            this.value = Utils.divideFixed(value).getKey();
            System.out.println("check: "+this.value);
        }
        this.dValue = Utils.divideFixed(value).getValue();
        System.out.println("Int"+this.value);
        System.out.println("Dec"+this.dValue);
        this.bitSize = mbitSize;
        this.nbitSize = nbitSize;
    }

    public NumericType(String type, BigInteger mValue, BigInteger nValue,int mbitSize, int nbitSize) {

    }

    public int getNBitSize() {
        return nbitSize;
    }

    @Override
    public String getTypeAsString() {
        return type;
    }

    @Override
    public BigInteger getValue() {
        return value;
    }

    public BigDecimal getDValue() {
        return dValue;
    }

    public BigInteger getNValue() {
        return nValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NumericType that = (NumericType) o;

        if (!type.equals(that.type)) {
            return false;
        }
        if(this.nbitSize==0) {
            return Objects.equals(value, that.value);
        }else {
            System.out.println("nbitSize:" + nbitSize);
            System.out.println(value);
            System.out.println(dValue);
            System.out.println("decimalValue"+decimalValue.stripTrailingZeros().toPlainString());
            System.out.println("that.decimalValue"+that.decimalValue.stripTrailingZeros().toPlainString());
            // return (value.equals(that.value)&&dValue.equals(that.dValue));
            if(decimalValue.scale()<that.decimalValue.scale()){
                System.out.println(1);
                return (dValue.compareTo(that.dValue.setScale(dValue.scale(),BigDecimal.ROUND_HALF_UP))==0&&value.equals(that.value));
            }else {
                System.out.println(2);
                System.out.println(dValue.setScale(that.dValue.scale(),BigDecimal.ROUND_HALF_UP));
                System.out.println(that.decimalValue);
                return (dValue.setScale(that.dValue.scale(),BigDecimal.ROUND_HALF_UP).compareTo(that.dValue)==0&&value.equals(that.value));
            }
        }
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    public int getBitSize() {
        return bitSize;
    }

    public void setBitSize(int bitSize) {
        this.bitSize = bitSize;
    }
}
