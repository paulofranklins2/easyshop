package org.yearup.model.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Simple authority/role wrapper used by Spring Security.
 */
@Setter
@Getter
@ToString
public class Authority {
    private String name;

    public Authority(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authority authority = (Authority) o;
        return name.equals(authority.name);
    }
}
