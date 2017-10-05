
package com.sos.joc.model.calendar;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * calendar
 * <p>
 * 
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "path",
    "name",
    "type",
    "category",
    "title",
    "from",
    "to",
    "includes",
    "excludes"
})
public class Calendar {

    /**
     * non negative long
     * <p>
     * 
     * 
     */
    @JsonProperty("id")
    private Long id;
    @JsonProperty("path")
    private String path;
    @JsonProperty("name")
    private String name;
    /**
     * calendar type
     * <p>
     * 
     * 
     */
    @JsonProperty("type")
    private CalendarType type = CalendarType.fromValue("WORKING_DAYS");
    @JsonProperty("category")
    private String category;
    @JsonProperty("title")
    private String title;
    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     */
    @JsonProperty("from")
    private String from;
    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     */
    @JsonProperty("to")
    private String to;
    /**
     * frequencies
     * <p>
     * 
     * 
     */
    @JsonProperty("includes")
    private Frequencies includes;
    /**
     * frequencies
     * <p>
     * 
     * 
     */
    @JsonProperty("excludes")
    private Frequencies excludes;

    /**
     * non negative long
     * <p>
     * 
     * 
     * @return
     *     The id
     */
    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    /**
     * non negative long
     * <p>
     * 
     * 
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The path
     */
    @JsonProperty("path")
    public String getPath() {
        return path;
    }

    /**
     * 
     * @param path
     *     The path
     */
    @JsonProperty("path")
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * calendar type
     * <p>
     * 
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public CalendarType getType() {
        return type;
    }

    /**
     * calendar type
     * <p>
     * 
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(CalendarType type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The category
     */
    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    /**
     * 
     * @param category
     *     The category
     */
    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * 
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @return
     *     The from
     */
    @JsonProperty("from")
    public String getFrom() {
        return from;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @param from
     *     The from
     */
    @JsonProperty("from")
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @return
     *     The to
     */
    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    /**
     * date
     * <p>
     * ISO date YYYY-MM-DD
     * 
     * @param to
     *     The to
     */
    @JsonProperty("to")
    public void setTo(String to) {
        this.to = to;
    }

    /**
     * frequencies
     * <p>
     * 
     * 
     * @return
     *     The includes
     */
    @JsonProperty("includes")
    public Frequencies getIncludes() {
        return includes;
    }

    /**
     * frequencies
     * <p>
     * 
     * 
     * @param includes
     *     The includes
     */
    @JsonProperty("includes")
    public void setIncludes(Frequencies includes) {
        this.includes = includes;
    }

    /**
     * frequencies
     * <p>
     * 
     * 
     * @return
     *     The excludes
     */
    @JsonProperty("excludes")
    public Frequencies getExcludes() {
        return excludes;
    }

    /**
     * frequencies
     * <p>
     * 
     * 
     * @param excludes
     *     The excludes
     */
    @JsonProperty("excludes")
    public void setExcludes(Frequencies excludes) {
        this.excludes = excludes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(path).append(name).append(type).append(category).append(title).append(from).append(to).append(includes).append(excludes).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Calendar) == false) {
            return false;
        }
        Calendar rhs = ((Calendar) other);
        return new EqualsBuilder().append(id, rhs.id).append(path, rhs.path).append(name, rhs.name).append(type, rhs.type).append(category, rhs.category).append(title, rhs.title).append(from, rhs.from).append(to, rhs.to).append(includes, rhs.includes).append(excludes, rhs.excludes).isEquals();
    }

}
