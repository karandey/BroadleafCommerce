/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.broadleafcommerce.profile.test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.profile.domain.Customer;
import org.broadleafcommerce.profile.domain.IdGeneration;
import org.broadleafcommerce.profile.domain.IdGenerationImpl;
import org.broadleafcommerce.profile.service.CustomerService;
import org.broadleafcommerce.profile.test.dataprovider.CustomerDataProvider;
import org.broadleafcommerce.test.integration.BaseTest;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.test.annotation.Rollback;
import org.testng.annotations.Test;

public class CustomerTest extends BaseTest {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    @Resource
    private CustomerService customerService;

    @Resource
    private UserDetailsService userDetailsService;

    List<Long> userIds = new ArrayList<Long>();

    List<String> userNames = new ArrayList<String>();

    @Test(groups = { "createCustomerIdGeneration" })
    @Rollback(false)
    public void createCustomerIdGeneration() {
        IdGeneration idGeneration = new IdGenerationImpl();
        idGeneration.setType("org.broadleafcommerce.profile.domain.Customer");
        idGeneration.setBatchStart(1L);
        idGeneration.setBatchSize(10L);
        em.persist(idGeneration);
    }

    @Test(groups = "createCustomers", dependsOnGroups="createCustomerIdGeneration", dataProvider = "setupCustomers", dataProviderClass = CustomerDataProvider.class)
    @Rollback(false)
    public void createCustomer(Customer customerInfo) {
        Customer customer = customerService.createCustomerFromId(null);
        customer.setPassword(customerInfo.getPassword());
        customer.setUsername(customerInfo.getUsername());
        Long customerId = customer.getId();
        assert customerId != null;
        customer = customerService.saveCustomer(customer);
        assert customer.getId() == customerId;
        userIds.add(customer.getId());
        userNames.add(customer.getUsername());
    }

    @Test(groups = { "readCustomer" }, dependsOnGroups = { "createCustomers" })
    public void readCustomersById() {
        for (Long userId : userIds) {
            Customer customer = customerService.readCustomerById(userId);
            assert customer.getId() == userId;
        }
    }

    @Test(groups = { "readCustomer1" }, dependsOnGroups = { "createCustomers" })
    public void readCustomersByUsername1() {
        for (String userName : userNames) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            assert userDetails != null && userDetails.getPassword().equals(userDetails.getUsername() + "Password");
        }
    }

    @Test(groups = { "changeCustomerPassword" }, dependsOnGroups = { "readCustomer1" })
    @Rollback(false)
    public void changeCustomerPasswords() {
        for (String userName : userNames) {
            Customer customer = customerService.readCustomerByUsername(userName);
            customer.setPassword(customer.getPassword() + "-Changed");
            customerService.saveCustomer(customer);
        }
    }

    @Test(groups = { "readCustomer2" }, dependsOnGroups = { "changeCustomerPassword" })
    public void readCustomersByUsername2() {
        for (String userName : userNames) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
            assert userDetails != null && userDetails.getPassword().equals(userDetails.getUsername() + "Password-Changed");
        }
    }
}