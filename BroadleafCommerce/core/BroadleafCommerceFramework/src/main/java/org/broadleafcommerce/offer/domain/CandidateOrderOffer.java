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
package org.broadleafcommerce.offer.domain;

import java.io.Serializable;

import org.broadleafcommerce.order.domain.Order;
import org.broadleafcommerce.util.money.Money;

public interface CandidateOrderOffer extends Serializable {

    public void setId(Long id);

    public Long getId();

    public Money getDiscountedPrice(); //transient, computed

    public Money getDiscountAmount(); //transient, computed

    public Order getOrder();

    public void setOrder(Order order);

    public int getPriority(); // convenience offer.getPriority()

    public Offer getOffer();

    public void setOffer(Offer offer);

}
