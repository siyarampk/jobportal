package com.eazybytes.jobportal.scopes;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
@Getter
@Setter
public class ApplicationScopedBean {

    private int visitorsCount;

    public ApplicationScopedBean() {
        System.out.println("ApplicationScopedBean created");
    }

    public Integer incrementVisitorsCount(){
      return   visitorsCount++;
    }

}
