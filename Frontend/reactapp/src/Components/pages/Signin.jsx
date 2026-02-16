import React, { useMemo, useState } from "react";
import Error from "../../responseDisplay/Error";
import Success from "../../responseDisplay/Success";
import UserAccountApi from "../../services/UserAccountApi";
import { Oval } from "react-loader-spinner";
import { FaTimes } from "react-icons/fa";
import { motion } from "framer-motion";
import { SlideLeft } from "../../animation/direction";
import { useNavigate } from "react-router-dom";

const OTP_LEN = 6;

function UserLogin() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [loading, setLoading] = useState(false);
  const [otpLoading, setOtpLoading] = useState(false);
  const [resendLoading, setResendLoading] = useState(false);

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [otpBar, setOtpBar] = useState(false);
  const [otp, setOtp] = useState(Array(OTP_LEN).fill(""));

  const otpValue = useMemo(() => otp.join(""), [otp]);

  const validateLogin = () => {
    if (!email.trim() || !password.trim()) {
      setError("Email and password are required.");
      return false;
    }
    return true;
  };

  const extractAuth = (res) => {
    if (!res) return null;
    if (res.token) return res;
    if (res.data && res.data.token) return res.data;
    return null;
  };

  const saveSessionAndNavigate = (authResponse) => {
    const token = authResponse.token;
    const role = authResponse.role;
    const userId = authResponse.userId;

    localStorage.setItem("token", token);
    localStorage.setItem("role", role);
    localStorage.setItem("userId", String(userId));

    if (role === "ADMIN") navigate("/admin");
   // else if (role === "USER") navigate("/user");
    else if (role === "VENDOR") navigate("/");
    else if (role === "PUBLISHER") navigate("/");
    else navigate("/");
  };

  const handleLogin = async () => {
    setError("");
    setSuccess("");

    if (!validateLogin()) return;

    setLoading(true);
    try {
      const res = await UserAccountApi.login({ email: email.trim(), password });
      const auth = extractAuth(res);

      if (auth?.token) {
        setSuccess("Login successful.");
        saveSessionAndNavigate(auth);
        return;
      }

      setError("Unexpected login response.");
    } catch (e) {
      const msg =
        e?.response?.data?.message ||
        e?.response?.data ||
        e?.message ||
        "Login failed";

      if (String(msg).includes("Email not verified")) {
        setOtpBar(true);
        setSuccess("Email not verified. Please enter OTP to verify.");
      } else {
        setError(String(msg));
      }
    } finally {
      setLoading(false);
    }
  };

  const handleOtpChange = (value, index) => {
    if (!/^\d?$/.test(value)) return;
    const next = [...otp];
    next[index] = value;
    setOtp(next);

    if (value && index < OTP_LEN - 1) {
      document.getElementById(`otp-input-${index + 1}`)?.focus();
    }
  };

  const verifyOtp = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (otpValue.length !== OTP_LEN) {
      setError("Please enter a valid 6-digit OTP.");
      return;
    }

    setOtpLoading(true);
    try {
      const verifyRes = await UserAccountApi.verifyEmailOtp({
        email: email.trim(),
        otp: otpValue,
      });

      setSuccess("Email verified. Logging you in...");

      const loginRes = await UserAccountApi.login({
        email: email.trim(),
        password,
      });

      const auth2 = extractAuth(loginRes);

      if (auth2?.token) {
        setOtpBar(false);
        saveSessionAndNavigate(auth2);
        return;
      }

      setError("Verified, but login response unexpected.");
    } catch (e2) {
      const msg =
        e2?.response?.data?.message ||
        e2?.response?.data ||
        e2?.message ||
        "OTP verification failed";
      setError(String(msg));
    } finally {
      setOtpLoading(false);
    }
  };

  const resendOtp = async () => {
    setError("");
    setSuccess("");

    if (!email.trim()) {
      setError("Enter your email first.");
      return;
    }

    setResendLoading(true);
    try {
      const r = await UserAccountApi.resendVerifyOtp({ email: email.trim() });
      setSuccess("OTP resent. Check your email.");
    } catch (e) {
      const msg =
        e?.response?.data?.message ||
        e?.response?.data ||
        e?.message ||
        "Failed to resend OTP";
      setError(String(msg));
    } finally {
      setResendLoading(false);
    }
  };

  return (
    <div className="bg-white min-h-screen flex items-center justify-center px-4">
      <div className="w-full max-w-md bg-gray-100 p-8 rounded-2xl shadow-md">
        <motion.div
          variants={SlideLeft(0.2)}
          initial="hidden"
          whileInView="visible"
          className="flex flex-col"
        >
          <h1 className="text-3xl font-bold text-center text-blue-900 mb-6">
            Sign In
          </h1>

          {error && <Error error={error} setError={setError} />}
          {success && <Success success={success} setSuccess={setSuccess} />}

          <div className="mt-4">
            <label className="text-blue-900 font-semibold mb-1 block">
              Email
            </label>
            <input
              type="email"
              placeholder="name@gmail.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full rounded-lg bg-white p-2 text-sm outline-none border focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="mt-4">
            <label className="text-blue-900 font-semibold mb-1 block">
              Password
            </label>
            <input
              type="password"
              placeholder="Your password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full rounded-lg bg-white p-2 text-sm outline-none border focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <button
            onClick={handleLogin}
            disabled={loading}
            className={`mt-6 w-full rounded-lg px-4 py-2 text-sm font-semibold text-gray-100 transition
              ${
                loading
                  ? "bg-blue-700/60 cursor-not-allowed"
                  : "bg-blue-700 hover:bg-blue-600"
              }`}
          >
            {loading ? (
              <div className="flex items-center justify-center">
                <Oval
                  height={20}
                  width={20}
                  color="white"
                  visible={true}
                  ariaLabel="oval-loading"
                  secondaryColor="white"
                  strokeWidth={3}
                  strokeWidthSecondary={3}
                />
              </div>
            ) : (
              "Sign In"
            )}
          </button>

          <p className="mt-4 text-center text-sm text-blue-700">
            Don't have an account?{" "}
            <a href="/Sign-up" className="underline">
              Register here
            </a>
          </p>
        </motion.div>
      </div>

      {/* OTP MODAL */}
      {otpBar && (
        <div className="fixed inset-0 bg-black/70 flex items-center justify-center px-4 z-50">
          <div className="relative w-full max-w-md rounded-2xl bg-white p-6 shadow-2xl">
            <button
              className="absolute right-4 top-4 text-gray-600"
              onClick={() => setOtpBar(false)}
            >
              <FaTimes size={18} />
            </button>

            <h2 className="text-xl font-semibold text-center text-blue-900">
              Verify your email
            </h2>
            <p className="mt-2 text-center text-sm text-blue-700">
              Enter the 6-digit OTP sent to <b>{email || "your email"}</b>
            </p>

            <form onSubmit={verifyOtp} className="mt-6">
              <div className="flex justify-center gap-2">
                {otp.map((v, index) => (
                  <input
                    key={index}
                    id={`otp-input-${index}`}
                    type="text"
                    inputMode="numeric"
                    maxLength={1}
                    value={v}
                    onChange={(e) => handleOtpChange(e.target.value, index)}
                    onFocus={(e) => e.target.select()}
                    className="w-12 h-12 rounded-lg border text-center text-xl outline-none focus:ring-2 focus:ring-blue-500"
                  />
                ))}
              </div>

              <button
                type="submit"
                disabled={otpLoading}
                className={`mt-6 w-full rounded-lg px-4 py-2 text-sm font-semibold text-gray-100 transition
                  ${
                    otpLoading
                      ? "bg-blue-700/60 cursor-not-allowed"
                      : "bg-blue-700 hover:bg-blue-600"
                  }`}
              >
                {otpLoading ? "Verifying..." : "Verify OTP"}
              </button>

              <button
                type="button"
                onClick={resendOtp}
                disabled={resendLoading}
                className="mt-3 w-full rounded-lg px-4 py-2 text-sm font-semibold border border-slate-300 hover:bg-slate-50"
              >
                {resendLoading ? "Resending..." : "Resend OTP"}
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default UserLogin;