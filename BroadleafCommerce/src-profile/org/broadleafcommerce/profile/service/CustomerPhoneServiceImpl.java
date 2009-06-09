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
package org.broadleafcommerce.profile.service;

import java.util.List;

import javax.annotation.Resource;

import org.broadleafcommerce.profile.dao.CustomerPhoneDao;
import org.broadleafcommerce.profile.domain.CustomerPhone;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("blCustomerPhoneService")
public class CustomerPhoneServiceImpl implements CustomerPhoneService {

    @Resource
    protected CustomerPhoneDao customerPhoneDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerPhone saveCustomerPhone(CustomerPhone customerPhone) {
        List<CustomerPhone> activeCustomerPhones = readActiveCustomerPhonesByCustomerId(customerPhone.getCustomer().getId());
        if (activeCustomerPhones != null && activeCustomerPhones.size() == 0) {
            customerPhone.getPhone().setDefault(true);
        } else {
            // if parameter customerPhone is set as default, unset all other default phones
            if (customerPhone.getPhone().isDefault()) {
                for (CustomerPhone activeCustomerPhone : activeCustomerPhones) {
                    if (activeCustomerPhone.getId() != customerPhone.getId() && activeCustomerPhone.getPhone().isDefault()) {
                        activeCustomerPhone.getPhone().setDefault(false);
                        customerPhoneDao.save(activeCustomerPhone);
                    }
                }
            }
        }
        return customerPhoneDao.save(customerPhone);
    }

    public List<CustomerPhone> readActiveCustomerPhonesByCustomerId(Long customerId) {
        return customerPhoneDao.readActiveCustomerPhonesByCustomerId(customerId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerPhone readCustomerPhoneByIdAndCustomerId(Long customerPhoneId, Long customerId) {
        return customerPhoneDao.readCustomerPhoneByIdAndCustomerId(customerPhoneId, customerId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void makeCustomerPhoneDefault(Long customerPhoneId, Long customerId) {
        customerPhoneDao.makeCustomerPhoneDefault(customerPhoneId, customerId);
    }

    public void deleteCustomerPhoneByIdAndCustomerId(Long customerPhoneId, Long customerId){
        customerPhoneDao.deleteCustomerPhoneByIdAndCustomerId(customerPhoneId, customerId);
    }

    @Override
    public CustomerPhone findDefaultCustomerPhone(Long customerId) {
        return customerPhoneDao.findDefaultCustomerPhone(customerId);
    }

    @Override
    public List<CustomerPhone> readAllCustomerPhonesByCustomerId(Long customerId) {
        return customerPhoneDao.readAllCustomerPhonesByCustomerId(customerId);
    }
}