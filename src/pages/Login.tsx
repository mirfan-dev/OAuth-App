import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Github, Chrome, Lock, CheckCircle2Icon, EyeOff, Eye } from "lucide-react";
import { motion } from "framer-motion";
import { useState } from "react";
import type LoginData from "@/models/LoginData";
import { useNavigate } from "react-router";
import toast from "react-hot-toast";
import { loginUser } from "@/services/AuthService";
import { Alert, AlertTitle } from "@/components/ui/alert";
import { Spinner } from "@/components/ui/spinner";
import useAuth from "@/auth/store";

function Login() {
  const [data, setData] = useState<LoginData>({
    email: "",
    password: "",
  });

  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<any>(null);
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const login= useAuth((state)=>state.login);

  // handle form change
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setData((value) => ({
      ...value,
      [event.target.name]: event.target.value,
    }));
  };

  // handle form submit

  const handleFormSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log("FORM DATA:", data);

    if (data.email.trim() === "") {
      toast.error("Email is required");
      return;
    }
    if (data.password.trim() === "") {
      toast.error("Password is required");
      return;
    }

    // form submit for Login

    try {
      setLoading(true);
      // const result = await loginUser(data);
      // console.log(result);

      //login function
      const userInfo= await login(data);
      console.log(userInfo);
      toast.success("User Login Successfully");
      setData({
        email: "",
        password: "",
      });

      // save the current use logged in information in localstorage

      navigate("/dashboard");
    } catch (error) {
      setLoading(false);
      console.log(error);
      setError(error);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-black via-slate-900 to-black px-4">
      <motion.div
        initial={{ opacity: 0, y: 40 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="w-full max-w-md"
      >
        <Card className="bg-slate-900/80 backdrop-blur border-slate-800 rounded-3xl shadow-xl">
          <CardHeader className="space-y-2 text-center">
            <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-indigo-500/10">
              <Lock className="h-6 w-6 text-indigo-400" />
            </div>
            <h1 className="text-3xl font-bold bg-gradient-to-r from-indigo-400 to-cyan-400 bg-clip-text text-transparent">
              Welcome Back
            </h1>
            <p className="text-slate-400 text-sm">
              Sign in to your account to continue
            </p>
          </CardHeader>

          <CardContent>
            {/* Error Section */}
            {error && (
              <div className="mt-4">
                <Alert variant="destructive">
                  <CheckCircle2Icon className="h-4 w-4" />
                  <AlertTitle>
                    {error?.response?.data?.message ||
                      "Invalid email or password"}
                  </AlertTitle>
                </Alert>
              </div>
            )}

            <form onSubmit={handleFormSubmit} className="mt-4 space-y-6">
              {/* Email */}
              <div className="space-y-2">
                <label className="text-sm text-slate-300">Email</label>
                <Input
                  id="email"
                  type="email"
                  placeholder="you@example.com"
                  name="email"
                  value={data.email}
                  onChange={handleInputChange}
                  className="rounded-xl bg-slate-950 border-slate-800"
                />
              </div>

              {/* Password */}
              <div className="space-y-2">
                <label className="text-sm text-slate-300">Password</label>

                <div className="relative">
                  {/* Lock icon */}
                  <Lock className="absolute left-3 top-3 h-4 w-4 text-slate-500" />

                  {/* Password input */}
                  <Input
                    id="password"
                    type={showPassword ? "text" : "password"} // ✅ FIX
                    placeholder=".........."
                    name="password"
                    value={data.password}
                    onChange={handleInputChange}
                    className="pl-10 pr-10 rounded-xl bg-slate-950 border-slate-800" // ✅ pr-10 for eye
                  />

                  {/* Eye toggle */}
                  <button
                    type="button"
                    onClick={() => setShowPassword((prev) => !prev)} // ✅ safer toggle
                    className="absolute right-3 top-3 text-slate-500 hover:text-slate-300"
                  >
                    {showPassword ? (
                      <EyeOff className="h-4 w-4" />
                    ) : (
                      <Eye className="h-4 w-4" />
                    )}
                  </button>
                </div>
              </div>

              {/* Login Button */}
              <Button disabled={loading} className="w-full rounded-2xl ">
                {loading ? (
                  <>
                    {" "}
                    <Spinner /> "Please wait...."{" "}
                  </>
                ) : (
                  "Login"
                )}
              </Button>

              {/* Divider */}
              <div className="relative">
                <div className="absolute inset-0 flex items-center">
                  <span className="w-full border-t border-slate-800" />
                </div>
                <div className="relative flex justify-center text-xs uppercase">
                  <span className="bg-slate-900 px-2 text-slate-500">
                    Or continue with
                  </span>
                </div>
              </div>

              {/* OAuth Buttons */}
              <div className="grid grid-cols-2 gap-4">
                <Button
                  variant="outline"
                  className="rounded-xl border-slate-800"
                >
                  <Chrome className="mr-2 h-4 w-4" /> Google
                </Button>
                <Button
                  variant="outline"
                  className="rounded-xl border-slate-800"
                >
                  <Github className="mr-2 h-4 w-4" /> GitHub
                </Button>
              </div>

              {/* Footer */}
              <p className="text-center text-sm text-slate-400">
                Don’t have an account?{" "}
                <a href="/Signup" className="text-indigo-400 hover:underline">
                  Signup
                </a>
              </p>
            </form>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}

export default Login;
