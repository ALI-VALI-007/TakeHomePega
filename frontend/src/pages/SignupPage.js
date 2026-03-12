import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './LoginPage.css';

export default function SignupPage() {
  const { signUp, confirmSignUp, cognitoConfigured, canUseApp, loading } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [needsVerification, setNeedsVerification] = useState(false);
  const [verificationCode, setVerificationCode] = useState('');
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (!loading && canUseApp) navigate('/', { replace: true });
  }, [loading, canUseApp, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }
    if (password.length < 8) {
      setError('Password must be at least 8 characters');
      return;
    }
    setSubmitting(true);
    try {
      await signUp(email.trim(), password);
      setNeedsVerification(true);
    } catch (err) {
      setError(err.message || 'Sign up failed');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="login-page">
        <div className="login-box">Loading...</div>
      </div>
    );
  }

  if (!cognitoConfigured) {
    return (
      <div className="login-page">
        <div className="login-box login-box--message">
          <h1>Reading List</h1>
          <p>Cognito is not configured. Sign up is only available when Cognito is set up.</p>
          <p className="auth-link"><Link to="/login">Back to sign in</Link></p>
        </div>
      </div>
    );
  }

  const handleVerifySubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSubmitting(true);
    try {
      await confirmSignUp(email, verificationCode);
      navigate('/', { replace: true });
    } catch (err) {
      setError(err.message || 'Verification failed');
    } finally {
      setSubmitting(false);
    }
  };

  if (needsVerification) {
    return (
      <div className="login-page">
        <div className="login-box">
          <h1>Verify your email</h1>
          <p className="login-subtitle">
            We sent a verification code to <strong>{email}</strong>. Enter it below.
          </p>
          <form onSubmit={handleVerifySubmit}>
            {error && <div className="login-error">{error}</div>}
            <input
              type="text"
              placeholder="Verification code"
              value={verificationCode}
              onChange={(e) => setVerificationCode(e.target.value)}
              autoComplete="one-time-code"
              required
            />
            <button type="submit" disabled={submitting}>
              {submitting ? 'Verifying…' : 'Verify and sign in'}
            </button>
          </form>
          <p className="auth-link">
            <Link to="/login">Back to sign in</Link>
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="login-page">
      <div className="login-box">
        <h1>Reading List</h1>
        <p className="login-subtitle">Create an account</p>
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
            autoComplete="new-password"
            required
          />
          <input
            type="password"
            placeholder="Confirm password"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            autoComplete="new-password"
            required
          />
          <button type="submit" disabled={submitting}>
            {submitting ? 'Creating account…' : 'Sign up'}
          </button>
        </form>
        <p className="auth-link">
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </div>
    </div>
  );
}
