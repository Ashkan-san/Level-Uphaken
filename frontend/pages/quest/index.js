import Head from 'next/head'
import Image from 'next/image'
import Card from '../../components/card'
import { getSession } from 'next-auth/client'
import { requestAPI } from '../../utils/request'

export default function Dashboard({ profile, quests }) {

  const questsSummary = quests.map(quest => {
    const { finishedLevelSystems, ...remainingQuest } = quest
    return {
      ...remainingQuest,
      done: profile.levelsystem && profile.levelsystem.finishedQuests.some(elm => elm.id === quest.id),
    }
  })

  return (
    <>
      <Head>
        <title>Quests</title>
      </Head>
      <div className="page-heading">
        <div className="page-title">
          <div className="row">
            <div className="col mb-3">
              <h3>Quests</h3>
            </div>
          </div>
        </div>
        <section className="section row">
          <Card className="offset-3 col-6" bodyClassName="d-flex">
            <Image src='/images/qwest.png' width='100%' height='auto' />
            {(questsSummary.length > 0 && (
              <ul className="widget-todo-list-wrapper ps-0" style={{ width: '100%' }} id="widget-todo-list">
                {questsSummary.map(quest =>
                  <div key={quest.id} className="widget-todo-title-wrapper d-flex justify-content-between align-items-center m-4">
                    <div className="widget-todo-title-area d-flex align-items-center">
                      {(quest.done &&
                        <div className="badge bg-success mx-3" style={{ minWidth: '70px' }}>Erledigt</div>
                      ) || (
                          <div className="badge bg-danger mx-3" style={{ minWidth: '70px' }}>Offen</div>
                        )}
                      <span className="widget-todo-title ms-1"><b>{quest.title}</b></span>
                    </div>
                    <div className="widget-todo-item-action d-flex align-items-center">
                      <div className="badge bg-warning mx-3" style={{ minWidth: '70px' }}>{quest.rewardXP} XP</div>
                    </div>
                  </div>
                )}
              </ul>
            )) || (
                <div className="text-center">
                  Keine Quests vorhanden 😭
                </div>
              )}
          </Card>
        </section>
      </div>
    </>
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

  try {
    const profile = await requestAPI(`${process.env.API_ENDPOINT}/users/info`, session)
    const quests = await requestAPI(`${process.env.API_ENDPOINT}/quests`, session)
    return {
      props: { session, profile, quests }
    }
  } catch (error) {
    return {
      redirect: {
        destination: '/auth/logout',
        permanent: false,
      }
    }
  }
}