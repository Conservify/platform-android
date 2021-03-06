/*
 * Copyright (c) 2014 Ushahidi.
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program in the file LICENSE-AGPL. If not, see
 * https://www.gnu.org/licenses/agpl-3.0.html
 */
package com.ushahidi.android.core.usecase.user;

import com.ushahidi.android.core.entity.User;
import com.ushahidi.android.core.exception.ErrorWrap;
import com.ushahidi.android.core.usecase.IInteractor;

public interface IGetUser extends IInteractor {

    /**
     * Executes this use case.
     *
     * @param callback     A {@link IGetUser.Callback} used to notify the client.
     * @param userId The user ID
     */
    void execute(long userId, Callback callback);

    /**
     * Notify client when a user is successfully loaded or an error occurred in the process
     */
    interface Callback {

        void onUserLoaded(User user);

        void onError(ErrorWrap error);
    }
}
