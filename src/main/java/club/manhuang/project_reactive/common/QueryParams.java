package club.manhuang.project_reactive.common;

import org.springframework.util.LinkedMultiValueMap;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class QueryParams extends LinkedMultiValueMap<String,String> {

    @Min(0)
    private int page;

    @Min(1)
    @Max(500)
    private int pageSize;

    private String conditions;

    private String childConditions;

    private String customFieldConditions;

    private String orderBy;

    private String fields;

    private String columns;

    public QueryParams(){
        super(new LinkedHashMap<>());
        this.page = 0;
        this.pageSize = 50;
        this.add("page",String.valueOf(this.page));
        this.add("pageSize",String.valueOf(this.pageSize));
        this.add("orderBy","id asc");
    }

    public QueryParams(int inittialCapacity){
        super(new LinkedHashMap<>(inittialCapacity));
    }

    public QueryParams(Map<String, List<String>> otherMap){
        super(new LinkedHashMap<>(otherMap));
    }


    //TODO
    public static QueryParams withDefault(){
        QueryParams queryParams = new QueryParams();
        return queryParams.setPage(0).setPageSize(50).setOrderBy("id asc");
    }

    public int getPage(){
        return this.page;
    }


    public QueryParams setPage(int page) {
        this.put("page",List.of(String.valueOf(page)));
        this.page = page;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public QueryParams setPageSize(int pageSize){
        this.put("pageSize",List.of(String.valueOf(pageSize)));
        this.pageSize = pageSize;
        return this;
    }

    public String getConditions() {
        return conditions;
    }

    public QueryParams setConditions(String conditions) {
        this.put("conditions",List.of(conditions));
        this.conditions = conditions;
        return this;
    }

    public String getChildConditions(){
        return this.childConditions;
    }

    public QueryParams setChildConditions(String childConditions){
        this.put("childConditions",List.of(childConditions));
        this.childConditions = childConditions;
        return this;
    }


    public String getCustomFieldConditions() {
        return customFieldConditions;
    }


    public QueryParams setCustomFieldConditions(String customFieldConditions) {
        this.put("customFieldConditions",List.of(customFieldConditions));
        this.customFieldConditions = customFieldConditions;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public QueryParams setOrderBy(String orderBy){
        this.put("orderBy",List.of(orderBy));
        this.orderBy = orderBy;
        return this;
    }

    public String getFields() {
        return fields;
    }

    public QueryParams setFields(String fields){
        this.put("fields",List.of(fields));
        this.fields = fields;
        return this;
    }

    public String getColumns() {
        return columns;
    }

    public QueryParams setColumns(String columns){
        this.put("columns",List.of(columns));
        this.columns = columns;
        return this;
    }
}
