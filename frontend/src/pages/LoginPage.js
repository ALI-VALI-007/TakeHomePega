import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './LoginPage.css';

export default function LoginPage() {
  const { signIn, cognitoConfigured, canUseApp, loading } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [submitting, setSubmitting] = useState(false);

  const from = location.state?.from?.pathname || '/';

  useEffect(() => {
    if (!loading && canUseApp) navigate(from, { replace: true });
  }, [loading, canUseApp, from, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await signIn(email.trim(), password);
      navigate(from, { replace: true });
    } catch (err) {
      setError(err.message || 'Sign in failed');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return <div className="login-page"><div className="login-box">Loading...</div></div>;
  }

  if (!cognitoConfigured) {
    return (
      <div className="login-page">
        <div className="login-box login-box--message">
          <h1>Reading List</h1>
          <p>Cognito is not configured. Set REACT_APP_USER_POOL_ID, REACT_APP_USER_POOL_CLIENT_ID, and REACT_APP_AWS_REGION in .env to enable login.</p>
          <p>For development, set REACT_APP_DEV_USER_ID in .env and restart; you will be redirected to the app.</p>
          {process.env.REACT_APP_DEV_USER_ID && (
            <button type="button" className="btn btn-primary" onClick={() => navigate('/', { replace: true })}>
              Continue to app (dev)
            </button>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className="login-page">
      <div className="login-box">
        <h1>Reading List</h1>
        <p className="login-subtitle">Sign in</p>
        <form onSubmit={handleSubmit}>
          {error && <div className="login-error">{error}</div>}
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            autoComplete="email"
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            autoComplete="current-password"
            required
          />
          <button type="submit" disabled={submitting}>
            {submitting ? 'Signing in…' : 'Sign in'}
          </button>
        </form>
        <p className="auth-link">
          Don&apos;t have an account? <Link to="/signup">Sign up</Link>
        </p>
      </div>
    </div>
  );
}
