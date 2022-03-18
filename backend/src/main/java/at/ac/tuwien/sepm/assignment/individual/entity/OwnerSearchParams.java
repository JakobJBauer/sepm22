package at.ac.tuwien.sepm.assignment.individual.entity;

public class OwnerSearchParams {
    private String searchTerm;
    private Integer resultSize;

    public OwnerSearchParams(String searchTerm, Integer resultSize) {
        this.searchTerm = searchTerm;
        this.resultSize = resultSize;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public Integer getResultSize() {
        return resultSize;
    }

    public void setResultSize(Integer resultSize) {
        this.resultSize = resultSize;
    }
}
