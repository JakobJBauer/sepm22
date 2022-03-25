package at.ac.tuwien.sepm.assignment.individual.entity;

public class ParentSearchParams extends OwnerSearchParams{
    private Sex sex;

    public ParentSearchParams(String searchTerm, Sex sex, Integer resultSize) {
        super(searchTerm, resultSize);
        this.sex = sex;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
