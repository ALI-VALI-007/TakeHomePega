import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { Amplify } from 'aws-amplify';
import { getCurrentUser, signIn, signOut as amplifySignOut, signUp, confirmSignUp, autoSignIn, fetchAuthSession } from 'aws-amplify/auth';

const config = {
  userPoolId: process.env.REACT_APP_USER_POOL_ID,
  userPoolClientId: process.env.REACT_APP_USER_POOL_CLIENT_ID,
  region: process.env.REACT_APP_AWS_REGION || 'us-east-1',
};

if (config.userPoolId && config.userPoolClientId) {
  Amplify.configure({
    Auth: {
      Cognito: {
        userPoolId: config.userPoolId,
        userPoolClientId: config.userPoolClientId,
        identityPoolId: process.env.REACT_APP_IDENTITY_POOL_ID || undefined,
      },
    },
  });
}

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [userId, setUserId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [cognitoConfigured, setCognitoConfigured] = useState(
    !!(config.userPoolId && config.userPoolClientId)
  );

  const devUserId = process.env.REACT_APP_DEV_USER_ID;

  const loadUser = useCallback(async () => {
    if (!cognitoConfigured) {
      if (devUserId) setUserId(devUserId);
      setLoading(false);
      return;
    }
    try {
      const u = await getCurrentUser();
      const session = await fetchAuthSession();
      const sub = session?.tokens?.idToken?.payload?.sub ?? u?.userId;
      setUser(u);
      setUserId(sub || u?.userId || u?.username);
    } catch {
      setUser(null);
      setUserId(null);
    } finally {
      setLoading(false);
    }
  }, [cognitoConfigured]);

  useEffect(() => {
    loadUser();
  }, [loadUser, devUserId]);

  const signInUser = async (email, password) => {
    await signIn({ username: email.trim(), password });
    await loadUser();
  };

  const signUpUser = async (email, password) => {
    const e = email.trim();
    await signUp({
      username: e,
      password,
      options: {
        userAttributes: { email: e },
        autoSignIn: true,
      },
    });
  };

  const confirmSignUpUser = async (email, confirmationCode) => {
    const e = email.trim();
    await confirmSignUp({ username: e, confirmationCode: confirmationCode.trim() });
    await autoSignIn();
    await loadUser();
  };

  const signOutUser = async () => {
    if (cognitoConfigured) await amplifySignOut();
    setUser(null);
    setUserId(null);
  };

  const canUseApp = cognitoConfigured ? !!user : !!devUserId;

  const value = {
    user,
    userId,
    loading,
    cognitoConfigured,
    canUseApp,
    signIn: signInUser,
    signUp: signUpUser,
    confirmSignUp: confirmSignUpUser,
    signOut: signOutUser,
    refreshUser: loadUser,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
}
