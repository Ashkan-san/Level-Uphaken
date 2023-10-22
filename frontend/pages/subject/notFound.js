import Link from "next/link";
import SingleCardPage from "../../components/singleCardPage";
import { getSession } from 'next-auth/client';

export default function NotFound() {
    return (
        <SingleCardPage title="Unbekannter Kurs" headertext="Der Kurs konnte nicht gefunden werden">
            Zur Suche deiner Kurse nutze bitte die Seitenleiste oder die
            <Link href="/subject/">
                <a>
                <span> Kurs&uuml;bersicht </span>
                </a>
            </Link>
        </SingleCardPage>
    )
}

export async function getServerSideProps(context) {
    const session = await getSession(context)
  
    if (!session) {
      return {
        redirect: {
          destination: '/auth/login',
          permanent: false,
        }
      }
    }
  
    return {
      props: {}
    }
  }