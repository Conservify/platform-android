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

package com.ushahidi.android.data.api;

import com.ushahidi.android.data.BaseTestCase;
import com.ushahidi.android.data.api.auth.AccessToken;
import com.ushahidi.android.data.api.auth.Payload;
import com.ushahidi.android.data.api.service.UserService;
import com.ushahidi.android.data.exception.NetworkConnectionException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;

import android.content.Context;

import java.io.IOException;

import retrofit.Callback;
import retrofit.client.Response;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Ushahidi Team <team@ushahidi.com>
 */
public class UserApiTest extends BaseTestCase {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    AccessToken mMockAccessToken;

    private Context mMockContext;

    @Spy
    private Payload mSpyPayload = new Payload("", "", "", "", "", "");

    @Mock
    private IUserApi.UserAccountLoggedInCallback mUserAccountLoggedInCallback;

    @Mock
    private UserService mMockUserService;

    @Mock
    private Callback<AccessToken> mMockCallback;


    private UserApi mUserApi;


    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        mMockContext = Robolectric.application.getBaseContext();
        mUserApi = spy(new UserApi(mMockContext, mMockUserService));
    }

    @Test
    public void shouldInvalidateConstructorsWithNullArguments() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Context cannot be null.");
        mUserApi = new UserApi(null, null);
    }

    @Test
    public void shouldLoginUserAccountSuccessfully() throws IOException {

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((Callback<AccessToken>) invocation
                        .getArguments()[1]).success(mMockAccessToken, any(Response.class));
                return null;
            }
        }).when(mMockUserService).getAccessToken(any(Payload.class), any(Callback.class));

        given(mUserApi.isDeviceConnectedToInternet(mMockContext)).willReturn(true);

        mUserApi.loginUserAccount(mSpyPayload, mUserAccountLoggedInCallback);

        verify(mUserAccountLoggedInCallback).onUserAccountLoggedIn(mMockAccessToken);
    }

    @Test
    public void shouldLoginUserAccountFailBecauseNoInternetConnect() throws IOException {

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ((IUserApi.UserAccountLoggedInCallback) invocation
                        .getArguments()[1]).onError(any(NetworkConnectionException.class));
                return null;
            }
        }).when(mMockUserService).getAccessToken(any(Payload.class), any(Callback.class));

        given(mUserApi.isDeviceConnectedToInternet(mMockContext)).willReturn(false);

        mUserApi.loginUserAccount(mSpyPayload, mUserAccountLoggedInCallback);

        verify(mUserAccountLoggedInCallback, times(1))
                .onError(any(NetworkConnectionException.class));
    }
}
