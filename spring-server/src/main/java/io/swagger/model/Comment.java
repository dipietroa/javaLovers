package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Comment
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2018-10-24T16:40:41.784Z")

public class Comment   {
  @JsonProperty("name")
  private String name = null;

  @JsonProperty("text")
  private String text = null;

  public Comment name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(min=3) 
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Comment text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Get text
   * @return text
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull

@Size(max=300) 
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(this.name, comment.name) &&
        Objects.equals(this.text, comment.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, text);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Comment {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

