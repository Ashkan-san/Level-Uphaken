import NextAuth from 'next-auth'
import Providers from 'next-auth/providers'
import axios from 'axios'

export default NextAuth({
  providers: [
    Providers.Credentials({
      name: 'HAW Account',
      async authorize(credentials) {
        try {
          const response = await axios.post(`${process.env.API_ENDPOINT}/users/login`, {
            emailAddress: credentials.email,
            password: credentials.password,
          })
          if (response.data) {
            return response.data
          }
          throw '/auth/login?error=Fehler beim Anmelden!&email=' + credentials.email
        } catch (error) {
          console.error(error);
          const errorMessage = error.response ? error.response.data.message : error.message
          throw '/auth/login?error=' + errorMessage + '&email=' + credentials.email
        }
      }
    })
  ],
  secret: process.env.SECRET,

  session: {
    jwt: true,
    maxAge: 30 * 24 * 60 * 60, // 30 days
  },

  jwt: {
    encryption: true,
    secret: process.env.JWT_SECRET,
    signingKey: process.env.JWT_SIGNING_KEY,
    encryptionKey: process.env.JWT_ENCRYPTION_KEY,
  },

  pages: {
    signIn: '/auth/login',  // Displays signin buttons
    signOut: '/auth/logout', // Displays form with sign out button
    error: '/auth/login', // Error code passed in query string as ?error=
  },

  // Callbacks are asynchronous functions you can use to control what happens
  // when an action is performed.
  // https://next-auth.js.org/configuration/callbacks
  callbacks: {
    async jwt(token, user) {
      if (user) {
        token.email = user.emailAddress;
        token.name = `${user.firstName} ${user.lastName}`;
        token.role = user.role;
        token.session = user.session;
      }
      return token;
    },
    async session(session, token) {
      session.user = {}
      session.user.email = token.email;
      session.user.name = token.name;
      session.user.role = token.role;
      session.user.session = token.session;
      return session;
    },
    async redirect(url, baseUrl) {
        return Promise.resolve(url)
    },
  },

  // Events are useful for logging
  // https://next-auth.js.org/configuration/events
  events: {},

  // Enable debug messages in the console if you are having problems
  debug: true,
})