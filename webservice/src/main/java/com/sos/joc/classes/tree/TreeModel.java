package com.sos.joc.classes.tree;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.sos.joc.model.tree.Tree;


public class TreeModel extends Tree {
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getPath()).append(getName()).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tree) == false) {
            return false;
        }
        Tree rhs = ((Tree) other);
        return new EqualsBuilder().append(getPath(), rhs.getPath()).append(getName(), rhs.getName()).isEquals();
    }

}
