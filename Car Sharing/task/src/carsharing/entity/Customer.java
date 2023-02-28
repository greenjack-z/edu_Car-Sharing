package carsharing.entity;

public class Customer {
    private final int id;
    private final String name;
    private int carId;

    public Customer(int id, String name, int carId) {
        this.id = id;
        this.name = name;
        this.carId = carId;
    }

    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Integer getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
