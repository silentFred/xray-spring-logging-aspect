package za.co.webler.advices;

public class TestEntity {
    private int id;
    private String name;

    public TestEntity() {
    }

    public TestEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @Override
//    public String toString() {
//        return "za.co.webler.advices.TestEntity {\"id\":" + this.id + ",\"name\":\"" + this.name + "\"}";
//    }


    @Override
    public String toString() {
        return "TestEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
