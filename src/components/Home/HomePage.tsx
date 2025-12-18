import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { ShieldCheck, Lock, KeyRound, Github, Chrome, Zap, Users } from "lucide-react";
import { motion } from "framer-motion";

export default function HomePage() {
  return (
    <div className="min-h-screen bg-gradient-to-r from-black via-slate-900 to-black text-white">
      {/* Hero Section */}
      <section className="container mx-auto px-6 py-24 text-center">
        <motion.h1
          initial={{ opacity: 0, y: 30 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.6 }}
          className="text-5xl md:text-6xl font-bold bg-gradient-to-r from-indigo-400 to-cyan-400 bg-clip-text text-transparent"
        >
          Secure Authentication for the Future
        </motion.h1>
        <p className="mt-6 text-lg text-slate-300 max-w-2xl mx-auto">
          A modern authentication platform with Email/Password, OAuth2,
          JWT security, and enterprise-grade protection.
        </p>
        <div className="mt-10 flex justify-center gap-4">
          <Button size="lg" className="rounded-2xl">Get Started</Button>
          <Button size="lg" variant="outline" className="rounded-2xl">
            View Docs
          </Button>
        </div>
      </section>

      {/* OAuth Providers */}
      <section className="container mx-auto px-6 py-16">
        <h2 className="text-3xl font-semibold text-center mb-10">OAuth Providers</h2>
        <div className="grid grid-cols-1 sm:grid-cols-3 gap-6">
          <Card className="bg-slate-900 border-slate-800">
            <CardContent className="flex items-center gap-4 p-6">
              <Chrome className="w-8 h-8 text-red-400" />
              <span>Login with Google</span>
            </CardContent>
          </Card>
          <Card className="bg-slate-900 border-slate-800">
            <CardContent className="flex items-center gap-4 p-6">
              <Github className="w-8 h-8" />
              <span>Login with GitHub</span>
            </CardContent>
          </Card>
          <Card className="bg-slate-900 border-slate-800">
            <CardContent className="flex items-center gap-4 p-6">
              <KeyRound className="w-8 h-8 text-yellow-400" />
              <span>Passwordless OAuth</span>
            </CardContent>
          </Card>
        </div>
      </section>

      {/* Features Section */}
      <section className="container mx-auto px-6 py-20">
        <h2 className="text-3xl font-semibold text-center mb-12">Core Features</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <Feature icon={<ShieldCheck />} title="Enterprise Security" desc="JWT, role-based access, and encrypted sessions." />
          <Feature icon={<Lock />} title="Zero Trust" desc="Secure APIs with token-based validation." />
          <Feature icon={<Zap />} title="High Performance" desc="Built with Spring Boot, React & Vite." />
        </div>
      </section>

      {/* Use Cases */}
      <section className="container mx-auto px-6 py-20 bg-slate-950 rounded-3xl">
        <h2 className="text-3xl font-semibold text-center mb-12">Who Is It For?</h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <UseCase title="Developers" icon={<Users />} desc="Plug & play authentication APIs." />
          <UseCase title="Startups" icon={<Zap />} desc="Launch faster with secure login." />
          <UseCase title="Enterprises" icon={<ShieldCheck />} desc="Scale securely with OAuth2." />
        </div>
      </section>

      {/* CTA */}
      <section className="container mx-auto px-6 py-24 text-center">
        <h2 className="text-4xl font-bold">Ready to Secure Your App?</h2>
        <p className="mt-4 text-slate-300">Integrate authentication in minutes.</p>
        <div className="mt-8">
          <Button size="lg" className="rounded-2xl">Start Free</Button>
        </div>
      </section>

      {/* Footer */}
      <footer className="border-t border-slate-800 py-8 text-center text-slate-400">
        © 2025 Authify • Secure OAuth Platform
      </footer>
    </div>
  );
}

function Feature({ icon, title, desc }: any) {
  return (
    <Card className="bg-slate-900 border-slate-800">
      <CardContent className="p-6 space-y-4">
        <div className="w-10 h-10">{icon}</div>
        <h3 className="text-xl font-semibold">{title}</h3>
        <p className="text-slate-400">{desc}</p>
      </CardContent>
    </Card>
  );
}

function UseCase({ icon, title, desc }: any) {
  return (
    <Card className="bg-slate-900 border-slate-800">
      <CardContent className="p-6 space-y-4 text-center">
        <div className="flex justify-center">{icon}</div>
        <h3 className="text-xl font-semibold">{title}</h3>
        <p className="text-slate-400">{desc}</p>
      </CardContent>
    </Card>
  );
}
