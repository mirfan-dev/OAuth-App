import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import {
  Github,
  Chrome,
  UserPlus,
  User,
  Mail,
  Lock,
  CheckCircle2Icon,
  EyeOff,
  Eye,
} from "lucide-react";
import { motion } from "framer-motion";
import { useRef, useState } from "react";
import toast from "react-hot-toast";
import type { RegisterData } from "@/models/RegisterData";
import { registerData } from "@/services/AuthService";
import { useNavigate } from "react-router";
import { Alert, AlertTitle } from "@/components/ui/alert";
import { Spinner } from "@/components/ui/spinner";
import ReCAPTCHA from "react-google-recaptcha";

function Signup() {
  const [data, setData] = useState<RegisterData>({
    name: "",
    email: "",
    password: "",
    image: "",
    recaptchaToken: "",
  });

  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<any>(null);
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const recaptchaRef = useRef<any>(null);
  const SITE_KEY = import.meta.env.VITE_RECAPTCHA_SITE_KEY;

  // handle form change
  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    // console.log(event.target.name);
    // console.log(event.target.value);
    setData((value) => ({
      ...value,
      [event.target.name]: event.target.value,
    }));
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onloadend = () => {
      setData((prev) => ({
        ...prev,
        image: reader.result as string, // ✅ base64
      }));
    };
    reader.readAsDataURL(file);
  };

  // handle form submit

  const handleFormSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    console.log("FORM DATA:", data);

    // validation
    if (data.name.trim() === "") {
      toast.error("Name is required");
      return;
    }

    if (data.email.trim() === "") {
      toast.error("Email is required");
      return;
    }
    if (data.password.trim() === "") {
      toast.error("Password is required");
      return;
    }
    if (!data.image) {
      toast.error("Profile image is required");
      return;
    }

    // form submit for registration

    try {
      setLoading(true);

      const result = await registerData(data);
      console.log(result);
      toast.success("User registered Successfully");
      setData({
        name: "",
        email: "",
        password: "",
        image: "",
        recaptchaToken: "",
      });
      recaptchaRef.current?.reset();
      navigate("/login");
    } catch (error) {
      console.log(error);
      setError(error);
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-black via-slate-900 to-black px-4">
      <motion.div
        initial={{ opacity: 0, y: 40 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ duration: 0.6 }}
        className="w-full max-w-md"
      >
        <Card className="bg-slate-900/80 backdrop-blur border-slate-800 rounded-3xl shadow-2xl">
          <CardHeader className="space-y-2 text-center">
            <div className="mx-auto flex h-12 w-12 items-center justify-center rounded-full bg-indigo-500/10">
              <UserPlus className="h-6 w-6 text-indigo-400" />
            </div>
            <h1 className="text-3xl font-bold bg-gradient-to-r from-indigo-400 to-cyan-400 bg-clip-text text-transparent">
              Create Your Account
            </h1>
            <p className="text-slate-400 text-sm">
              Secure your access with next‑gen authentication
            </p>
          </CardHeader>

          {/* Error Section */}
          {error && (
            <div className="mt-4">
              <Alert variant="destructive">
                <CheckCircle2Icon className="h-4 w-4" />
                <AlertTitle>
                  {error?.response?.data?.message || "User Registration failed"}
                </AlertTitle>
              </Alert>
            </div>
          )}

          <CardContent>
            <form onSubmit={handleFormSubmit} className="space-y-6">
              {/* Name */}
              <div className="space-y-2">
                <label className="text-sm text-slate-300">Full Name</label>
                <div className="relative">
                  <User className="absolute left-3 top-3 h-4 w-4 text-slate-500" />
                  <Input
                    id="name"
                    type="text"
                    placeholder="John Doe"
                    name="name"
                    value={data.name}
                    onChange={handleInputChange}
                    className="pl-10 rounded-xl bg-slate-950 border-slate-800"
                  />
                </div>
              </div>

              {/* Email */}
              <div className="space-y-2">
                <label className="text-sm text-slate-300">Email</label>
                <div className="relative">
                  <Mail className="absolute left-3 top-3 h-4 w-4 text-slate-500" />
                  <Input
                    id="email"
                    type="email"
                    placeholder="you@example.com"
                    name="email"
                    value={data.email}
                    onChange={handleInputChange}
                    className="pl-10 rounded-xl bg-slate-950 border-slate-800"
                  />
                </div>
              </div>

              {/* Profile Image */}
              <div className="space-y-2">
                <label className="text-sm text-slate-300">Profile Image</label>

                <div className="relative">
                  {/* Left Icon */}
                  <User className="absolute left-3 top-3 h-4 w-4 text-slate-500" />

                  {/* File Input */}
                  <Input
                    type="file"
                    accept="image/*"
                    id="profile-image"
                    className="pl-10 pr-12 cursor-pointer rounded-xl bg-slate-900 text-sm text-slate-300 border border-slate-800 hover:bg-slate-800 file:cursor-pointer file:border-0 file:bg-indigo-600 file:px-3 file:py-1 file:text-white file:rounded-lg hover:file:bg-indigo-700"
                    onChange={handleImageChange}
                  />

                  {/* Image Preview inside input */}
                  {data.image && (
                    <img
                      src={data.image}
                      alt="Preview"
                      className="absolute right-3 top-1/2 h-6 w-6 -translate-y-1/2 rounded-md object-cover border border-slate-700"
                    />
                  )}
                </div>
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

              {/* reCAPTCHA */}
              <div className="space-y-2 flex justify-start ">
                <div className="scale-75 origin-left rounded-xl">
                  <ReCAPTCHA
                    ref={recaptchaRef}
                    sitekey={SITE_KEY}
                    onChange={(token) =>
                      setData((p) => ({ ...p, recaptchaToken: token ?? "" }))
                    }
                  />
                </div>
              </div>

              {/* Sign Up Button */}
              <Button className="w-full rounded-2xl mt-2">
                {loading ? (
                  <>
                    {" "}
                    <Spinner /> "Please wait...."{" "}
                  </>
                ) : (
                  "Create Account"
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
                Already have an account?{" "}
                <a href="/login" className="text-indigo-400 hover:underline">
                  Login
                </a>
              </p>
            </form>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}

export default Signup;
