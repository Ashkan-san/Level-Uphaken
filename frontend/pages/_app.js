import App from 'next/app'
import Head from 'next/head'
import { getSession, Provider } from 'next-auth/client'
import { requestAPI } from '../utils/request'

import Sidebar from '../components/sidebar'
import Header from '../components/header'
import Footer from '../components/footer'

import '../styles/bootstrap.scss'
import 'perfect-scrollbar/css/perfect-scrollbar.css'
import 'bootstrap-icons/font/bootstrap-icons.css'
import 'react-toastify/dist/ReactToastify.css'
import '../styles/app.scss'

function MyApp({ Component, pageProps, session, profile}) {
  return (
    <Provider options={{ clientMaxAge: 120, keepAlive: 30 }} session={ pageProps.session } >
      <Head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      </Head>
      <Sidebar session={session} profile={profile} />
      <div id="main" className="layout-navbar">
        <Header session={session} profile={profile} />
        <div id="main-content">
        <Component {...pageProps} session={session} profile={profile} />
        <Footer session={session} />
        </div>
      </div>
    </Provider>
  );
}

MyApp.getInitialProps = async (appContext) => {
    const appProps = await App.getInitialProps(appContext);
    const session = await getSession(appContext);
    
    let profile = {}
    if (session) {
      try {
       profile = await requestAPI(`${process.env.API_ENDPOINT}/users/info`, session)
      } catch (error) {
        console.error(error);
      }
    }

    return {...appProps, session, profile}
}

export default MyApp
