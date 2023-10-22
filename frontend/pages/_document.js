import React from 'react';
import Document, { Html, Head, Main, NextScript } from 'next/document';

export default class MyDocument extends Document {

  static async getInitialProps(context) {
    const initialProps = await Document.getInitialProps(context)
    return { ...initialProps }
  }

  render() {
    return (
      <Html lang="de">
        <Head>
          <meta charSet="UTF-8" />
          <link rel="preconnect" href="https://fonts.gstatic.com" />
          <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Nunito:wght@300;400;600;700;800&display=swap" />
          <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
        </Head>
        <body>
          <div id="app">
            <Main />
          </div>
          <script src="/vendor/perfect-scrollbar.js"></script>
          <script src="/vendor/bootstrap.bundle.js"></script>
          <script src="/js/mazer.js"></script>
          <NextScript />
        </body>
      </Html>
    );
  }
}
