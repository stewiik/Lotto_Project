package pl.lotto.domain.numberreceiver;

public class HashGeneratorTestImpl implements HashGenerable {

    private final String hash;

    public HashGeneratorTestImpl() {
        this.hash = "123";
    }

    @Override
    public String getHash() {
        return hash;
    }
}
