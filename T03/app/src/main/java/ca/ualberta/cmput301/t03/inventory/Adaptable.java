package ca.ualberta.cmput301.t03.inventory;

import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Copyright 2015 John Slevinsky
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 */
public interface Adaptable<T> {
    /**
     * This should return a SORTED LIST of items,
     * sorted by ITEM NAME.
     * @return
     */
    public List<T> getAdaptableItems() throws IOException, ServiceNotAvailableException;
}
